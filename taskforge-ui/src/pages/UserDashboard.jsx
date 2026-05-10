import { useState, useEffect, useCallback } from 'react'
import { api } from '../api'

const STATUS_STYLE = {
  CREATED:          { bg: '#64748b22', color: '#94a3b8', border: '#475569' },
  ASSIGNED:         { bg: '#3b82f622', color: '#60a5fa', border: '#3b82f6' },
  IN_PROGRESS:      { bg: '#f59e0b22', color: '#fbbf24', border: '#f59e0b' },
  READY_FOR_REVIEW: { bg: '#8b5cf622', color: '#a78bfa', border: '#8b5cf6' },
  COMPLETED:        { bg: '#10b98122', color: '#34d399', border: '#10b981' },
  APPROVED:         { bg: '#06b6d422', color: '#22d3ee', border: '#06b6d4' },
}

function StatusChip({ status }) {
  const s = STATUS_STYLE[status] || {}
  return (
    <span className="status-chip" style={{ background: s.bg, color: s.color, borderColor: s.border }}>
      {status?.replace(/_/g, ' ')}
    </span>
  )
}

export default function UserDashboard({ user, onLogout }) {
  const [tab,     setTab]     = useState('tasks')
  const [tasks,   setTasks]   = useState([])
  const [profile, setProfile] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error,   setError]   = useState('')
  const [msg,     setMsg]     = useState('')
  const [editName,  setEditName]  = useState('')
  const [editEmail, setEditEmail] = useState('')

  const loadData = useCallback(async () => {
    setLoading(true)
    try {
      const [t, p] = await Promise.all([api.getMyTasks(), api.getMe()])
      setTasks(t)
      setProfile(p)
      setEditName(p.name)
      setEditEmail(p.email)
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { loadData() }, [loadData])

  const flash = (m) => { setMsg(m); setTimeout(() => setMsg(''), 3000) }

  const markReady = async (taskId) => {
    try {
      await api.markReady(taskId)
      flash('Marked as Ready for Review!')
      loadData()
    } catch (e) { setError(e.message) }
  }

  const updateProfile = async () => {
    try {
      await api.updateMe({ name: editName, email: editEmail })
      flash('Profile updated')
      loadData()
    } catch (e) { setError(e.message) }
  }

  const stats = {
    total:      tasks.length,
    inProgress: tasks.filter(t => t.taskStatus === 'IN_PROGRESS').length,
    done:       tasks.filter(t => ['COMPLETED', 'APPROVED'].includes(t.taskStatus)).length,
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="logo">⚡ TaskForge</div>
        <div className="user-info">
          <div className="user-badge user">USER</div>
          <div className="user-email">{user.email}</div>
        </div>
        <nav className="sidebar-nav">
          <button className={`nav-item ${tab === 'tasks' ? 'active' : ''}`} onClick={() => setTab('tasks')}>
            ▣ &nbsp;My Tasks
          </button>
          <button className={`nav-item ${tab === 'profile' ? 'active' : ''}`} onClick={() => setTab('profile')}>
            ◈ &nbsp;Profile
          </button>
        </nav>
        <button className="logout-btn" onClick={onLogout}>↩ Sign Out</button>
      </aside>

      <main className="content">
        {error && (
          <div className="alert error" onClick={() => setError('')}>
            <span>{error}</span><span>✕</span>
          </div>
        )}
        {msg && <div className="alert success"><span>{msg}</span></div>}

        {/* ───────── MY TASKS ───────── */}
        {tab === 'tasks' && (
          <>
            <h1 className="page-title">My Tasks</h1>

            <div className="stat-row">
              {[
                { label: 'Total',       value: stats.total,      color: '#60a5fa' },
                { label: 'In Progress', value: stats.inProgress, color: '#fbbf24' },
                { label: 'Done',        value: stats.done,       color: '#34d399' },
              ].map(s => (
                <div key={s.label} className="stat-card">
                  <div className="stat-label">{s.label}</div>
                  <div className="stat-value" style={{ color: s.color }}>{s.value}</div>
                </div>
              ))}
            </div>

            <div className="card">
              <div className="card-title">
                Assigned Tasks <span className="count-badge">{tasks.length}</span>
              </div>
              {loading ? (
                <div className="empty">Loading...</div>
              ) : tasks.length === 0 ? (
                <div className="empty">No tasks assigned to you yet.</div>
              ) : (
                <table className="table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Title</th>
                      <th>Description</th>
                      <th>Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {tasks.map(t => (
                      <tr key={t.id}>
                        <td className="text-mono">#{t.id}</td>
                        <td>{t.title}</td>
                        <td className="text-muted">{t.description || '—'}</td>
                        <td><StatusChip status={t.taskStatus} /></td>
                        <td>
                          {t.taskStatus === 'IN_PROGRESS' && (
                            <button className="btn sm primary" onClick={() => markReady(t.id)}>
                              Mark Ready ↑
                            </button>
                          )}
                          {t.taskStatus === 'ASSIGNED' && (
                            <span className="text-dim">Start working</span>
                          )}
                          {t.taskStatus === 'READY_FOR_REVIEW' && (
                            <span className="text-dim" style={{ color: '#a78bfa' }}>Under review</span>
                          )}
                          {['COMPLETED', 'APPROVED'].includes(t.taskStatus) && (
                            <span className="text-dim" style={{ color: '#34d399' }}>✓ Done</span>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </>
        )}

        {/* ───────── PROFILE ───────── */}
        {tab === 'profile' && (
          <>
            <h1 className="page-title">My Profile</h1>
            <div className="card" style={{ maxWidth: '460px' }}>
              <div className="form-group">
                <label>Name</label>
                <input
                  className="input"
                  value={editName}
                  onChange={e => setEditName(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  className="input"
                  type="email"
                  value={editEmail}
                  onChange={e => setEditEmail(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label>Role</label>
                {profile && (
                  <div style={{ marginTop: '4px' }}>
                    <span className={`role-chip ${profile.role.toLowerCase()}`}>{profile.role}</span>
                  </div>
                )}
              </div>
              <button className="btn primary" onClick={updateProfile}>
                Save Changes
              </button>
            </div>
          </>
        )}
      </main>
    </div>
  )
}
