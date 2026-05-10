import { useState, useEffect } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import AdminDashboard from './pages/AdminDashboard'
import UserDashboard from './pages/UserDashboard'
import { getToken, parseJwt, clearToken } from './api'

export default function App() {
  const [user,    setUser]    = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = getToken()
    if (token) {
      const p = parseJwt(token)
      if (p && p.exp * 1000 > Date.now()) {
        setUser({ role: p.role, email: p.sub, userId: p.userId })
      } else {
        clearToken()
      }
    }
    setLoading(false)
  }, [])

  const handleLogin = (token) => {
    const p = parseJwt(token)
    setUser({ role: p.role, email: p.sub, userId: p.userId })
  }

  const handleLogout = () => {
    clearToken()
    setUser(null)
  }

  if (loading) return <div className="loading-screen">⚡ TaskForge</div>

  const isAdmin = user?.role === 'ROLE_ADMIN'
  const isUser  = user?.role === 'ROLE_USER'
  const home    = isAdmin ? '/admin' : '/dashboard'

  return (
    <Routes>
      <Route
        path="/login"
        element={user ? <Navigate to={home} replace /> : <Login onLogin={handleLogin} />}
      />
      <Route
        path="/admin"
        element={isAdmin ? <AdminDashboard user={user} onLogout={handleLogout} /> : <Navigate to="/login" replace />}
      />
      <Route
        path="/dashboard"
        element={isUser ? <UserDashboard user={user} onLogout={handleLogout} /> : <Navigate to="/login" replace />}
      />
      <Route path="*" element={<Navigate to={user ? home : '/login'} replace />} />
    </Routes>
  )
}
