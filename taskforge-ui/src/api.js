const BASE = 'http://localhost:8080'

export const getToken   = ()  => localStorage.getItem('tf_token')
export const setToken   = (t) => localStorage.setItem('tf_token', t)
export const clearToken = ()  => localStorage.removeItem('tf_token')

export const parseJwt = (token) => {
  try { return JSON.parse(atob(token.split('.')[1])) }
  catch { return null }
}

const authHeaders = () => ({
  'Content-Type': 'application/json',
  ...(getToken() ? { Authorization: `Bearer ${getToken()}` } : {}),
})

const request = async (method, path, body) => {
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: authHeaders(),
    ...(body !== undefined ? { body: JSON.stringify(body) } : {}),
  })
  if (!res.ok) {
    const err = await res.json().catch(() => ({ message: res.statusText }))
    throw new Error(err.message || `Error ${res.status}`)
  }
  // approveTask returns 200 with empty body — guard against parsing it
  const ct = res.headers.get('content-type') || ''
  if (!ct.includes('application/json')) return null
  const text = await res.text()
  return text ? JSON.parse(text) : null
}

export const api = {
  // ── Auth
  login: (email, password) => request('POST', '/api/auth/login', { email, password }),

  // ── Admin — users
  getUsers:       ()         => request('GET',   '/api/admin/users'),
  createUser:     (data)     => request('POST',  '/api/admin/users', data),
  updateUserRole: (id, role) => request('PATCH', `/api/admin/users/${id}/role`, { role }),

  // ── Admin — tasks
  getTasks:         ()               => request('GET',   '/api/admin/tasks'),
  createTask:       (data)           => request('POST',  '/api/admin/tasks', data),
  assignTask:       (taskId, userId) => request('PATCH', `/api/admin/tasks/${taskId}/assign`, { userId }),
  updateTaskStatus: (taskId, status) => request('PATCH', `/api/admin/tasks/${taskId}/status`, { status }),
  completeTask:     (taskId)         => request('PATCH', `/api/admin/tasks/${taskId}/complete`),
  approveTask:      (taskId)         => request('PUT',   `/api/admin/tasks/${taskId}/approve`),

  // ── User
  getMyTasks: ()     => request('GET',   '/api/users/tasks'),
  markReady:  (id)   => request('PATCH', `/api/users/tasks/${id}/ready`),
  getMe:      ()     => request('GET',   '/api/users/me'),
  updateMe:   (data) => request('PATCH', '/api/users/me', data),
}
