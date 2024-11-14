import { getIdToken } from 'firebase/auth';
import { getInstallations, getToken } from 'firebase/installations';
import { app, auth } from '@/lib/firebase/config';

// Initialize Firebase app and auth
const installations = getInstallations(app);

// Fetch function with Firebase headers
export async function fetchWithFirebaseHeaders(request: Request) {
  const headers = new Headers(request.headers);

  // Fetch Firebase tokens concurrently
  const [authIdToken, installationToken] = await Promise.all([
    getAuthIdToken(auth),
    getToken(installations),
  ]);

  // Append tokens to the headers
  headers.append('Firebase-Instance-ID-Token', installationToken);
  if (authIdToken) {
    headers.append('Authorization', `Bearer ${authIdToken}`);
  }

  // Create a new request with the modified headers
  const newRequest = new Request(request, { headers });
  return await fetch(newRequest);
}

// Helper to get the authentication ID token
async function getAuthIdToken(auth: any) {
  await auth.authStateReady();
  if (!auth.currentUser) return null;
  return await getIdToken(auth.currentUser);
}
