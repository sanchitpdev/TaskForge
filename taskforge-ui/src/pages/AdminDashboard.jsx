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

const ALL_STATUSES = ['CREATED', 'ASSIGNED', 'IN_PROGRESS', 'READY_FOR_REVIEW', 'COMPLETED', 'APPROVED']

function StatusChip({ status }) {
  const s = STATUS_STYLE[status] || {}
  return (
    <span className="status-chip" style={{ background: s.bg, color: s.color, borderColor: s.border }}>
      {status?.replace(/_/g, ' ')}
    </span>
  )
}

export default function AdminDashboard({ user, onLogout }) {
  const [tab,     setTab]     = useState('tasks')
  const [tasks,   setTasks]   = useState([])
  const [users,   setUsers]   = useState([])
  const [loading, setLoading] = useState(true)
  const [error,   setError]   = useState('')
  const [msg,     setMsg]     = useState('')

  // Create task form state
  const [newTask, setNewTask] = useState({ title: '', description: '' })
  // Create user form state
  const [newUser, setNewUser] = useState({ name: '', email: '', password: '', role: 'USER' })
  // Assign task modal
  const [assignModal,  setAssignModal]  = useState(null) // { taskId }
  const [assignUserId, setAssignUserId] = useState('')
  // Update status modal
  const [statusModal,    setStatusModal]    = useState(null) // { taskId }
  const [selectedStatus, setSelectedStatus] = useState('')

  const loadAll = useCallback(async () => {
    setLoading(true)
    try {
      const [t, u] = await Promise.all([api.getTasks(), api.getUsers()])
      setTasks(t)
      setUsers(u)
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { loadAll() }, [loadAll])

  const flash = (m) => { setMsg(m); setTimeout(() => setMsg(''), 3000) }

  const run = async (fn, successMsg, after) => {
    try {
      await fn()
      flash(successMsg)
      if (after) after()
      loadAll()
    } catch (e) {
      setError(e.message)
    }
  }

  // Task actions
  const createTask = () => {
    if (!newTask.title.trim()) return
    run(
      () => api.createTask(newTask),
      'Task created',
      () => setNewTask({ title: '', description: '' })
    )
  }

  const doAssign = () => {
    if (!assignUserId) return
    run(
      () => api.assignTask(assignModal.taskId, Number(assignUserId)),
      'Task assigned',
      () => { setAssignModal(null); setAssignUserId('') }
    )
  }

  const doStatusUpdate = () => {
    if (!selectedStatus) return
    run(
      () => api.updateTaskStatus(statusModal.taskId, selectedStatus),
      'Status updated',
      () => { setStatusModal(null); setSelectedStatus('') }
    )
  }

  const completeTask = (taskId) => run(() => api.completeTask(taskId), 'Task completed')
  const approveTask  = (taskId) => run(() => api.approveTask(taskId),  'Task approved')

  // User actions
  const createUser = () => {
    if (!newUser.name || !newUser.email || !newUser.password) return
    run(
      () => api.createUser(newUser),
      'User created',
      () => setNewUser({ name: '', email: '', password: '', role: 'USER' })
    )
  }

  const changeRole = (id, currentRole) => {
    const next = currentRole === 'ADMIN' ? 'USER' : 'ADMIN'
    run(() => api.updateUserRole(id, next), `Role changed to ${next}`)
  }

  const getUserLabel = (uid) => {
    const u = users.find(u => u.id === uid)
    return u ? u.name : uid ? `#${uid}` : '—'
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="logo">⚡ TaskForge</div>
        <div className="user-info">
          <div className="user-badge">ADMIN</div>
          <div className="user-email">{user.email}</div>
        </div>
        <nav className="sidebar-nav">
          <button className={`nav-item ${tab === 'tasks' ? 'active' : ''}`} onClick={() => setTab('tasks')}>
            ▣ &nbsp;Tasks
          </button>
          <button className={`nav-item ${tab === 'users' ? 'active' : ''}`} onClick={() => setTab('users')}>
            ◈ &nbsp;Users
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

        {/* ───────── TASKS ───────── */}
        {tab === 'tasks' && (
          <>
            <h1 className="page-title">Task Management</h1>

            <div className="card">
              <div className="card-title">+ New Task</div>
              <div className="form-row">
                <input
                  className="input"
                  placeholder="Title *"
                  value={newTask.title}
                  onChange={e => setNewTask(p => ({ ...p, title: e.target.value }))}
                  style={{ flex: 2 }}
                />
                <input
                  className="input"
                  placeholder="Description (optional)"
                  value={newTask.description}
                  onChange={e => setNewTask(p => ({ ...p, description: e.target.value }))}
                  style={{ flex: 3 }}
                />
                <button className="btn primary" onClick={createTask} style={{ flexShrink: 0 }}>
                  Create
                </button>
              </div>
            </div>

            <div className="card">
              <div className="card-title">
                All Tasks <span className="count-badge">{tasks.length}</span>
              </div>
              {loading ? (
                <div className="empty">Loading...</div>
              ) : tasks.length === 0 ? (
                <div className="empty">No tasks yet. Create one above.</div>
              ) : (
                <table className="table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Title</th>
                      <th>Description</th>
                      <th>Status</th>
                      <th>Assigned To</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {tasks.map(t => (
                      <tr key={t.id}>
                        <td className="text-mono">#{t.id}</td>
                        <td>{t.title}</td>
                        <td className="text-muted">{t.description || '—'}</td>
                        <td><StatusChip status={t.taskStatus} /></td>
                        <td>{getUserLabel(t.assignedUserId)}</td>
                        <td>
                          <div className="col-actions">
                            <button
                              className="btn sm"
                              onClick={() => { setAssignModal({ taskId: t.id }); setAssignUserId('') }}
                            >
                              Assign
                            </button>
                            <button
                              className="btn sm"
                              onClick={() => { setStatusModal({ taskId: t.id }); setSelectedStatus(t.taskStatus) }}
                            >
                              Status
                            </button>
                            {t.taskStatus === 'READY_FOR_REVIEW' && (
                              <>
                                <button className="btn sm success" onClick={() => completeTask(t.id)}>
                                  Complete
                                </button>
                                <button className="btn sm approve" onClick={() => approveTask(t.id)}>
                                  Approve
                                </button>
                              </>
                            )}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </>
        )}

        {/* ───────── USERS ───────── */}
        {tab === 'users' && (
          <>
            <h1 className="page-title">User Management</h1>

            <div className="card">
              <div className="card-title">+ New User</div>
              <div className="form-row">
                <input
                  className="input"
                  placeholder="Name *"
                  value={newUser.name}
                  onChange={e => setNewUser(p => ({ ...p, name: e.target.value }))}
                />
                <input
                  className="input"
                  type="email"
                  placeholder="Email *"
                  value={newUser.email}
                  onChange={e => setNewUser(p => ({ ...p, email: e.target.value }))}
                />
                <input
                  className="input"
                  type="password"
                  placeholder="Password *"
                  value={newUser.password}
                  onChange={e => setNewUser(p => ({ ...p, password: e.target.value }))}
                />
                <select
                  className="input"
                  value={newUser.role}
                  onChange={e => setNewUser(p => ({ ...p, role: e.target.value }))}
                  style={{ flex: '0 0 90px' }}
                >
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
                <button className="btn primary" onClick={createUser} style={{ flexShrink: 0 }}>
                  Create
                </button>
              </div>
            </div>

            <div className="card">
              <div className="card-title">
                All Users <span className="count-badge">{users.length}</span>
              </div>
              {loading ? (
                <div className="empty">Loading...</div>
              ) : (
                <table className="table">
                  <thead>
                    <tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Actions</th></tr>
                  </thead>
                  <tbody>
                    {users.map(u => (
                      <tr key={u.id}>
                        <td className="text-mono">#{u.id}</td>
                        <td>{u.name}</td>
                        <td className="text-muted">{u.email}</td>
                        <td>
                          <span className={`role-chip ${u.role.toLowerCase()}`}>{u.role}</span>
                        </td>
                        <td>
                          <button className="btn sm" onClick={() => changeRole(u.id, u.role)}>
                            → {u.role === 'ADMIN' ? 'Make USER' : 'Make ADMIN'}
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </>
        )}
      </main>

      {/* ── Assign Modal ── */}
      {assignModal && (
        <div className="modal-overlay" onClick={() => setAssignModal(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h3>Assign Task #{assignModal.taskId}</h3>
            <select
              className="input"
              value={assignUserId}
              onChange={e => setAssignUserId(e.target.value)}
            >
              <option value="">Select a user...</option>
              {users.filter(u => u.role === 'USER').map(u => (
                <option key={u.id} value={u.id}>
                  {u.name} ({u.email})
                </option>
              ))}
            </select>
            <div className="modal-footer">
              <button className="btn" onClick={() => setAssignModal(null)}>Cancel</button>
              <button className="btn primary" onClick={doAssign}>Assign</button>
            </div>
          </div>
        </div>
      )}

      {/* ── Status Modal ── */}
      {statusModal && (
        <div className="modal-overlay" onClick={() => setStatusModal(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h3>Update Status — Task #{statusModal.taskId}</h3>
            <select
              className="input"
              value={selectedStatus}
              onChange={e => setSelectedStatus(e.target.value)}
            >
              {ALL_STATUSES.map(s => (
                <option key={s} value={s}>{s.replace(/_/g, ' ')}</option>
              ))}
            </select>
            <div className="modal-footer">
              <button className="btn" onClick={() => setStatusModal(null)}>Cancel</button>
              <button className="btn primary" onClick={doStatusUpdate}>Update</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
