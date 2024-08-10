import { env, createExecutionContext, waitOnExecutionContext, SELF } from 'cloudflare:test';
import { describe, it, expect } from 'vitest';
import worker from '../src';

describe('Cloudflare Workers KV API', () => {
	const token = 'sample_token'; // Example token that should match one in your AUTHORIZED_USERS
	const requestBaseUrl = 'http://localhost:8787'; // Use the appropriate base URL for your worker

	// Mocking authorized users and ENV
	const mockEnv = {
		AUTHORIZED_USERS: JSON.stringify([{ user: 'testUser', token }]),
		AUTHORIZED_USERS_SET: new Set([token]),
		AUTHORIZED_USERS_MAP_TOKEN_TO_USER: new Map([[token, 'testUser']]),
		SAFETO: {
			get: async (key) => {
				// Mock get behavior based on the key
				if (key === 'collection//key') {
					return { objects: [{ data: 'testData', _hidden: false }], _logs: [] };
				}
				return null; // Simulating not found
			},
			put: async () => { },
			list: async () => ({
				keys: [{ name: 'collection//key' }],
				cursor: null,
				list_complete: true
			})
		}
	}

	it('responds with Unauthorized for missing authorization header', async () => {
		const request = new Request(`${requestBaseUrl}/get/collection/key`, { method: 'GET' });
		const ctx = createExecutionContext();
		const response = await worker.fetch(request, mockEnv, ctx);
		await waitOnExecutionContext(ctx);
		expect(response.status).toBe(401);
		expect(await response.text()).toBe('Unauthorized');
	});

	it('responds with Unauthorized for invalid token', async () => {
		const request = new Request(`${requestBaseUrl}/get/collection/key`, {
			method: 'GET',
			headers: { authorization: 'Bearer invalid_token' }
		});
		const ctx = createExecutionContext();
		const response = await worker.fetch(request, mockEnv, ctx);
		await waitOnExecutionContext(ctx);
		expect(response.status).toBe(401);
		expect(await response.text()).toBe('Unauthorized');
	});

	it('retrieves an item with GET method', async () => {
		const request = new Request(`${requestBaseUrl}/get/collection/key`, {
			method: 'GET',
			headers: { authorization: `Bearer ${token}` }
		});
		const ctx = createExecutionContext();
		const response = await worker.fetch(request, mockEnv, ctx);
		await waitOnExecutionContext(ctx);
		const responseBody = await response.json();

		expect(response.status).toBe(200);
		expect(responseBody).toEqual({ data: 'testData' });
	});

	it('handles PUT requests correctly', async () => {
		const requestBody = JSON.stringify({ data: 'newData' });
		const request = new Request(`${requestBaseUrl}/put/collection/key`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
				authorization: `Bearer ${token}`
			},
			body: requestBody
		});
		const ctx = createExecutionContext();
		const response = await worker.fetch(request, mockEnv, ctx);
		await waitOnExecutionContext(ctx);

		expect(response.status).toBe(201); // Check for Created status
	});

	it('retrieves logs with HISTORY method', async () => {
		const request = new Request(`${requestBaseUrl}/history/collection/key`, {
			method: 'GET',
			headers: { authorization: `Bearer ${token}` }
		});
		const ctx = createExecutionContext();
		const response = await worker.fetch(request, mockEnv, ctx);
		await waitOnExecutionContext(ctx);
		const responseBody = await response.json();

		expect(response.status).toBe(200);
		expect(responseBody).toHaveProperty('logs');
	});

	it('responds with Method Not Allowed for unsupported methods', async () => {
		const request = new Request(`${requestBaseUrl}/get/collection/key`, {
			method: 'POST',
			headers: { authorization: `Bearer ${token}` }
		});
		const ctx = createExecutionContext();
		const response = await worker.fetch(request, mockEnv, ctx);
		await waitOnExecutionContext(ctx);

		expect(response.status).toBe(405);
		expect(await response.text()).toBe('Method Not Allowed');
	});

	// Additional tests for DELETE, log retrieval and LIST could be added similarly
});
