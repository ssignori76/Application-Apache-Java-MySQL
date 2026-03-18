const API_BASE = '/api/v1';

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    loadPublicServices();

    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        try {
            const res = await fetch(`${API_BASE}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            if (res.ok) {
                checkAuth();
            } else {
                showAlert('Login failed');
            }
        } catch (err) { showAlert('Error: ' + err); }
    });

    document.getElementById('logoutBtn').addEventListener('click', async () => {
        await fetch(`${API_BASE}/auth/logout`, { method: 'POST' });
        showSection('loginSection');
    });

    document.getElementById('refreshOrdersBtn').addEventListener('click', loadCustomerOrders);
    
    document.getElementById('buyServiceForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const serviceId = document.getElementById('serviceSelect').value;
        const note = document.getElementById('orderNote').value;
        try {
            const res = await fetch(`${API_BASE}/orders`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ serviceId, note })
            });
            if (res.ok) {
                showAlert('Order submitted successfully!');
                document.getElementById('buyServiceForm').reset();
                loadCustomerOrders();
            } else { showAlert('Failed to submit order'); }
        } catch (err) { showAlert('Error: ' + err); }
    });

    document.getElementById('filterOrdersBtn').addEventListener('click', loadAdminOrders);

    document.getElementById('userForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('userId').value;
        const payload = {
            username: document.getElementById('manageUsername').value,
            password: document.getElementById('managePassword').value,
            firstName: document.getElementById('manageFirstName').value,
            lastName: document.getElementById('manageLastName').value,
            role: document.getElementById('manageRole').value,
        };
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${API_BASE}/admin/users/${id}` : `${API_BASE}/admin/users`;
        try {
            const res = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            if (res.ok) {
                showAlert('User saved successfully');
                document.getElementById('userForm').reset();
                document.getElementById('userId').value = '';
                loadUsers();
            } else { showAlert('Failed to save user'); }
        } catch (err) { showAlert('Error: ' + err); }
    });

    document.getElementById('clearUserBtn').addEventListener('click', () => {
        document.getElementById('userForm').reset();
        document.getElementById('userId').value = '';
    });
});

async function loadPublicServices() {
    try {
        const res = await fetch(`${API_BASE}/services`);
        if (res.ok) {
            const services = await res.json();
            const tbody = document.querySelector('#publicServicesTable tbody');
            tbody.innerHTML = services.map(s => `<tr><td>${s.name}</td><td>${s.description}</td><td>${s.price}</td></tr>`).join('');
            
            const select = document.getElementById('serviceSelect');
            select.innerHTML = services.map(s => `<option value="${s.id}">${s.name} - $${s.price}</option>`).join('');
        }
    } catch (e) { console.error('Error loading services', e); }
}

async function checkAuth() {
    try {
        const res = await fetch(`${API_BASE}/me`);
        if (res.ok) {
            const user = await res.json();
            document.getElementById('userInfo').textContent = `Logged in as: ${user.firstName} ${user.lastName} (${user.role})`;
            showSection('authSection');
            if (user.role === 'ADMIN') {
                document.getElementById('adminArea').classList.remove('hidden');
                document.getElementById('customerArea').classList.add('hidden');
                loadAdminOrders();
                loadUsers();
            } else {
                document.getElementById('adminArea').classList.add('hidden');
                document.getElementById('customerArea').classList.remove('hidden');
                loadCustomerOrders();
            }
        } else {
            showSection('loginSection');
        }
    } catch (e) { showSection('loginSection'); }
}

async function loadCustomerOrders() {
    try {
        const res = await fetch(`${API_BASE}/orders`);
        if (res.ok) {
            const orders = await res.json();
            const tbody = document.querySelector('#customerOrdersTable tbody');
            tbody.innerHTML = orders.map(o => `<tr><td>${o.id}</td><td>${o.serviceName}</td><td>${o.status}</td><td>${o.note || ''}</td><td>${o.createdAt}</td></tr>`).join('');
        }
    } catch (e) { console.error(e); }
}

async function loadAdminOrders() {
    try {
        const status = document.getElementById('statusFilter').value;
        const url = status ? `${API_BASE}/admin/orders?status=${status}` : `${API_BASE}/admin/orders`;
        const res = await fetch(url);
        if (res.ok) {
            const orders = await res.json();
            const tbody = document.querySelector('#adminOrdersTable tbody');
            tbody.innerHTML = orders.map(o => `<tr><td>${o.id}</td><td>${o.customerName}</td><td>${o.serviceName}</td><td>${o.status}</td><td>${o.note || ''}</td><td>${o.createdAt}</td></tr>`).join('');
        }
    } catch (e) { console.error(e); }
}

async function loadUsers() {
    try {
        const res = await fetch(`${API_BASE}/admin/users`);
        if (res.ok) {
            const users = await res.json();
            const tbody = document.querySelector('#usersTable tbody');
            tbody.innerHTML = users.map(u => `<tr>
                <td>${u.id}</td>
                <td>${u.username}</td>
                <td>${u.firstName} ${u.lastName}</td>
                <td>${u.role}</td>
                <td>
                    <button onclick="editUser(${u.id}, '${u.username}', '${u.firstName}', '${u.lastName}', '${u.role}')">Edit</button>
                    <button onclick="deleteUser(${u.id})">Delete</button>
                </td>
            </tr>`).join('');
        }
    } catch (e) { console.error(e); }
}

window.editUser = function(id, username, firstName, lastName, role) {
    document.getElementById('userId').value = id;
    document.getElementById('manageUsername').value = username;
    document.getElementById('manageFirstName').value = firstName;
    document.getElementById('manageLastName').value = lastName;
    document.getElementById('manageRole').value = role;
};

window.deleteUser = async function(id) {
    if(confirm('Are you sure?')) {
        try {
            await fetch(`${API_BASE}/admin/users/${id}`, { method: 'DELETE' });
            loadUsers();
        } catch (e) { showAlert('Error deleting user'); }
    }
};

function showSection(sectionId) {
    document.getElementById('loginSection').classList.add('hidden');
    document.getElementById('authSection').classList.add('hidden');
    document.getElementById(sectionId).classList.remove('hidden');
}

function showAlert(msg) {
    const ab = document.getElementById('alertBox');
    ab.textContent = msg;
    ab.classList.remove('hidden');
    setTimeout(() => ab.classList.add('hidden'), 3000);
}