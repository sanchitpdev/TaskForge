import { useState } from 'react'
import { api, setToken } from '../api'

export default function Login({ onLogin }) {
  const [email,    setEmail]    = useState('')
  const [password, setPassword] = useState('')
  const [error,    setError]    = useState('')
  const [busy,     setBusy]     = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setBusy(true)
    try {
      const data = await api.login(email, password)
      setToken(data.accessToken)
      onLogin(data.accessToken)
    } catch (err) {
      setError(err.message || 'Login failed. Check your credentials.')
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="login-wrap">
      <div className="login-card">
        <div className="login-logo">⚡ TaskForge</div>
        <div className="login-sub">Task Management System</div>

        {error && <div className="alert error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              className="input"
              type="email"
              placeholder="you@example.com"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
              autoFocus
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input
              className="input"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
            />
          </div>
          <button
            type="submit"
            className="btn primary"
            style={{ width: '100%', padding: '10px', fontSize: '13px', marginTop: '6px', justifyContent: 'center' }}
            disabled={busy}
          >
            {busy ? 'Signing in...' : '→ Sign In'}
          </button>
        </form>

        <div className="demo-box">
          <div className="demo-label">Demo accounts</div>
          <div className="demo-grid">
            <span style={{ color: 'var(--primary)' }}>Admin</span>
            <span>admin1@example.com / admin123</span>
            <span style={{ color: 'var(--success)' }}>User</span>
            <span>user1@example.com / user123</span>
          </div>
        </div>
      </div>
    </div>
  )
}
