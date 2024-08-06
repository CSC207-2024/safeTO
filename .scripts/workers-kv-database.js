addEventListener('fetch', event => {
  event.respondWith(handleRequest(event.request));
});

// Assuming env.AUTHORIZED_USERS is already a JavaScript list of objects
const AUTH_TOKENS = new Set(env.AUTHORIZED_USERS.map(user => user.token));

// Main request handler
async function handleRequest(request) {
  const { method, url } = request;
  const path = new URL(url).pathname.split('/').filter(Boolean);

  // Basic authentication check
  const authHeader = request.headers.get('Authorization');
  const token = authHeader && authHeader.split(' ')[1];

  if (!AUTH_TOKENS.has(token)) {
    return new Response('Unauthorized', { status: 401 });
  }

  // Determine action based on requests
  const action = path[0];

  switch (action) {
    case 'get':
      return await handleGet(path.slice(1), method);
    case 'put':
      return await handlePut(path.slice(1), request, method);
    case 'delete':
      return await handleDelete(path.slice(1), method);
    case 'history':
      return await handleHistory(path.slice(1), method);
    default:
      return new Response('Method Not Allowed', { status: 405 });
  }
}

// GET function
async function handleGet(params, method) {
  if (method !== 'GET') {
    return new Response('Method Not Allowed', { status: 405 });
  }

  const [collection, key] = params;

  if (!collection || !key) {
    return new Response('Missing collection or key', { status: 400 });
  }

  const data = await MY_KV_STORE.get(`${collection}:${key}`);

  if (!data) {
    return new Response('Not Found', { status: 404 });
  }

  const versions = JSON.parse(data);
  const visibleVersions = versions.filter(version => !version.hidden);

  if (visibleVersions.length === 0) {
    return new Response('Not Found', { status: 404 });
  }

  return new Response(JSON.stringify(visibleVersions[visibleVersions.length - 1]), {
    headers: { 'Content-Type': 'application/json' },
  });
}

// PUT function
async function handlePut(params, request, method) {
  if (method !== 'PUT') {
    return new Response('Method Not Allowed', { status: 405 });
  }

  const [collection, key] = params;

  if (!collection || !key) {
    return new Response('Missing collection or key', { status: 400 });
  }

  const newValue = await request.json();

  if (!newValue || typeof newValue !== 'object') {
    return new Response('Invalid payload', { status: 400 });
  }

  const currentData = await MY_KV_STORE.get(`${collection}:${key}`);
  const versionHistory = currentData ? JSON.parse(currentData) : [];

  // Append new value with visible = true
  versionHistory.push({ ...newValue, hidden: false });
  await MY_KV_STORE.put(`${collection}:${key}`, JSON.stringify(versionHistory));

  return new Response('Created', { status: 201 });
}

// DELETE function
async function handleDelete(params, method) {
  if (method !== 'DELETE') {
    return new Response('Method Not Allowed', { status: 405 });
  }

  const [collection, key] = params;

  if (!collection || !key) {
    return new Response('Missing collection or key', { status: 400 });
  }

  const currentData = await MY_KV_STORE.get(`${collection}:${key}`);

  if (!currentData) {
    return new Response('Not Found', { status: 404 });
  }

  const versionHistory = JSON.parse(currentData);

  // Mark all versions as hidden
  const updatedHistory = versionHistory.map(version => ({ ...version, hidden: true }));
  await MY_KV_STORE.put(`${collection}:${key}`, JSON.stringify(updatedHistory));

  return new Response('Deleted', { status: 204 });
}

// HISTORY
