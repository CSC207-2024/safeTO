/**
 * - Run `npm run dev` in your terminal to start a development server
 * - Open a browser tab at http://localhost:8787/ to see your worker in action
 * - Run `npm run deploy` to publish your worker
 *
 * Learn more at https://developers.cloudflare.com/workers/
 */

export default {
	async fetch(request, env) {
		/**
		 * @typedef {Object} AuthorizedUser
		 * @property {string} user - The username of the authorized user.
		 * @property {string} token - The authorization token.
		 * @property {string} [comment] - An optional comment associated with the user.
		 */

		/**
		 * @type {AuthorizedUser[]}
		 */
		const AUTHORIZED_USERS = env.AUTHORIZED_USERS;
		const AUTHORIZED_USERS_SET = new Set(AUTHORIZED_USERS.map(obj => obj.token));
		const AUTHORIZED_USERS_MAP_TOKEN_TO_USER = new Map(AUTHORIZED_USERS.map(obj => [obj.token, obj.user]));
		const RESTRICTED_KEYS = new Set(['_logs']);

		const KV = env.SAFETO; // the Workers KV binding name

		const apiEnv = {
			AUTHORIZED_USERS,
			AUTHORIZED_USERS_SET,
			AUTHORIZED_USERS_MAP_TOKEN_TO_USER,
			RESTRICTED_KEYS,
			KV
		};

		return await handleRequest(request, apiEnv);
	},
};

function toPrimaryKey(collection, key) {
	return `${collection}//${key}`;
}

// Main request handler
async function handleRequest(request, apiEnv) {
	const url = new URL(request.url);
	const path = url.pathname.split('/').filter(Boolean);

	// Basic authentication check
	const authHeader = request.headers.get('authorization'); // format: authorization: Bearer <place_your_token_here>
	const token = authHeader && authHeader.split(' ')[1];

	if (!token || !apiEnv.AUTHORIZED_USERS_SET.has(token)) {
		return new Response('Unauthorized', { status: 401 });
	}

	// Look up the username associated with the token
	const username = apiEnv.AUTHORIZED_USERS_MAP_TOKEN_TO_USER.get(token);

	// Determine action based on request path
	const action = path[0];

	switch (action) {
		case 'get':
			return await handleGet(path.slice(1), username, request, apiEnv);
		case 'put':
			return await handlePut(path.slice(1), username, request, apiEnv);
		case 'delete':
			return await handleDelete(path.slice(1), username, request, apiEnv);
		case 'history':
			return await handleHistory(path.slice(1), username, request, apiEnv);
		case 'log':
			return await handleLog(path.slice(1), username, request, apiEnv);
		case 'list':
			return await handleList(path.slice(1), username, request, apiEnv);
		default:
			return new Response('Method Not Allowed', { status: 405 });
	}
}


// GET function
async function handleGet(params, username, request, apiEnv) {
	if (request.method !== 'GET') {
		return new Response('Method Not Allowed', { status: 405 });
	}

	const [collection, key] = params;

	if (!collection || !key) {
		return new Response('Missing collection or key', { status: 400 });
	}

	const primaryKey = toPrimaryKey(collection, key);
	const data = await apiEnv.KV.get(primaryKey, 'json');

	if (!data) {
		return new Response('Not Found', { status: 404 });
	}

	const { objects } = data;
	const visibleVersions = objects.filter(version => !version._hidden);

	if (visibleVersions.length === 0) {
		return new Response('Not Found', { status: 404 });
	}

	// Remove _hidden property from the response
	const responseObj = { ...visibleVersions[visibleVersions.length - 1] };
	delete responseObj._hidden;

	return new Response(JSON.stringify(responseObj), {
		headers: { 'Content-Type': 'application/json' },
	});
}

// PUT function
async function handlePut(params, username, request, apiEnv) {
	if (request.method !== 'PUT') {
		return new Response('Method Not Allowed', { status: 405 });
	}

	const [collection, key] = params;

	if (!collection || !key) {
		return new Response('Missing collection or key', { status: 400 });
	}

	if (apiEnv.RESTRICTED_KEYS.has(key)) {
		return new Response('Invalid key - unauthorized to modify this key', { status: 401 });
	}

	const newValue = await request.json();

	if (!newValue || typeof newValue !== 'object') {
		return new Response('Invalid payload', { status: 400 });
	}

	const primaryKey = toPrimaryKey(collection, key);

	if (newValue.hasOwnProperty('_hidden')) {
		// the _hidden is a reserved property. remove it if the user (un)intentionally set it
		delete newValue._hidden;
	}

	const currentData = await apiEnv.KV.get(primaryKey, 'json');
	const versionHistory = currentData || { objects: [], _logs: [] };

	// Append new value with _hidden = false
	versionHistory.objects.push({ ...newValue, _hidden: false });

	// Log the operation
	const logEntry = {
		method: 'PUT',
		user: username, // Store the authenticated username without exposing the tokens to logs
		time: Date.now() // Current time in UNIX timestamp
	};
	versionHistory._logs.push(logEntry);

	await apiEnv.KV.put(primaryKey, JSON.stringify(versionHistory));

	return new Response('Created', { status: 201 });
}

// DELETE function
async function handleDelete(params, username, request, apiEnv) {
	if (request.method !== 'DELETE') {
		return new Response('Method Not Allowed', { status: 405 });
	}

	const [collection, key] = params;

	if (!collection || !key) {
		return new Response('Missing collection or key', { status: 400 });
	}

	const primaryKey = toPrimaryKey(collection, key);

	const currentData = await apiEnv.KV.get(primaryKey, 'json');

	if (!currentData) {
		return new Response('Not Found', { status: 404 });
	}

	const versionHistory = currentData;

	// Mark all versions as hidden
	versionHistory.objects = versionHistory.objects.map(version => ({ ...version, _hidden: true }));

	await apiEnv.KV.put(primaryKey, JSON.stringify(versionHistory));

	// Log the delete operation
	const logEntry = {
		method: 'DELETE',
		user: username, // Store the authenticated username
		time: Date.now() // Current time in UNIX timestamp
	};
	versionHistory._logs.push(logEntry);
	await apiEnv.KV.put(toPrimaryKey(collection, '_logs'), JSON.stringify(versionHistory));

	return new Response('Deleted', { status: 204 });
}

// HISTORY function
async function handleHistory(params, username, request, apiEnv) {
	if (request.method !== 'GET') {
		return new Response('Method Not Allowed', { status: 405 });
	}

	const [collection, key] = params;

	if (!collection || !key) {
		return new Response('Missing collection or key', { status: 400 });
	}

	const primaryKey = toPrimaryKey(collection, key);
	const data = await apiEnv.KV.get(primaryKey, 'json');

	if (!data) {
		return new Response('Not Found', { status: 404 });
	}

	const { objects, _logs } = data;
	const visibleVersions = objects.filter(version => !version._hidden);

	return new Response(JSON.stringify({ versions: visibleVersions, logs: _logs }), {
		headers: { 'Content-Type': 'application/json' },
	});
}

// LOG function
async function handleLog(params, username, request, apiEnv) {
	if (request.method !== 'POST') {
		return new Response('Method Not Allowed', { status: 405 });
	}

	const [collection, key] = params;

	if (!collection || !key) {
		return new Response('Missing collection or key', { status: 400 });
	}

	const primaryKey = toPrimaryKey(collection, key);
	const currentData = await apiEnv.KV.get(primaryKey, 'json');

	if (!currentData) {
		return new Response('Not Found', { status: 404 });
	}

	const { _logs } = currentData;

	return new Response(JSON.stringify(_logs), {
		headers: { 'Content-Type': 'application/json' },
	});
}

// LIST function
async function handleList(params, username, request, apiEnv) {
	if (request.method !== 'GET') {
		return new Response('Method Not Allowed', { status: 405 });
	}

	const collection = params[0];

	if (!collection) {
		return new Response('Missing collection', { status: 400 });
	}

	const keys = await apiEnv.KV.list({ prefix: `${collection}//` });
	const results = [];

	for (const { name } of keys.keys) {
		const data = await apiEnv.KV.get(name, 'json');
		if (data) {
			const { objects } = data;
			const visibleVersions = objects.filter(version => !version._hidden);
			if (visibleVersions.length > 0) {
				// Return the latest visible version without _hidden property
				const latestVersion = { ...visibleVersions[visibleVersions.length - 1] };
				delete latestVersion._hidden;
				results.push({ key: name.replace(`${collection}//`, ''), value: latestVersion });
			}
		}
	}

	// Log the list operation
	const logEntry = {
		method: 'LIST',
		user: username,
		time: Date.now()
	};

	const logKey = toPrimaryKey(collection, '_logs');
	const logData = await apiEnv.KV.get(logKey, 'json');
	const logs = logData || { objects: [], _logs: [] };
	logs._logs.push(logEntry);
	await apiEnv.KV.put(logKey, JSON.stringify(logs));

	return new Response(JSON.stringify(results), {
		headers: { 'Content-Type': 'application/json' },
	});
}
