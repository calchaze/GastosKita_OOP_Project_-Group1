// ---------- helpers ----------
const $ = s => document.querySelector(s);
const peso = n => '₱' + Number(n).toLocaleString('en-PH', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
const esc = s => String(s).replace(/[&<>"']/g, c => ({ '&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;' }[c]));
const pad = n => String(n).padStart(2, '0');
const ymd = d => d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
const monthKey = (y, m) => y + '-' + pad(m + 1);

async function api(path, opts) {
  const res = await fetch(path, opts);
  const data = await res.json();
  if (!res.ok) throw new Error(data.error || 'Something went wrong.');
  return data;
}
const post = (path, obj) => api(path, {
  method: 'POST',
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  body: new URLSearchParams(obj)
});

function popup(message, kind, title) {
  $('#pTitle').textContent = title || 'GastosKita says...';
  $('#pMsg').textContent = message;
  $('#popup').className = kind || '';
  $('#popup').classList.remove('hidden');
}
$('#pClose').onclick = () => $('#popup').classList.add('hidden');
const kindOf = st => st === 'EXCEEDED' ? 'bad' : st === 'NEARING' ? 'warn' : '';

// ---------- navigation ----------
const loaders = { dashboard: loadDashboard, add: loadAdd, history: loadHistory,
                  budget: loadBudget, calendar: loadCalendar, breakdown: loadBreakdown };

$('#startBtn').onclick = () => {
  $('#welcome').classList.add('hidden');
  $('#app').classList.remove('hidden');
  show('dashboard');
};
document.querySelectorAll('#sidebar button').forEach(b => b.onclick = () => show(b.dataset.page));

function show(page) {
  document.querySelectorAll('.page').forEach(p => p.classList.add('hidden'));
  $('#page-' + page).classList.remove('hidden');
  document.querySelectorAll('#sidebar button').forEach(b => b.classList.toggle('active', b.dataset.page === page));
  loaders[page]().catch(e => popup(e.message, 'bad', 'Error'));
}

// ---------- dashboard ----------
async function loadDashboard() {
  const s = await api('/api/summary');
  $('#dBudget').textContent = peso(s.budget);
  $('#dSpent').textContent = peso(s.spent);
  $('#dRemain').textContent = peso(s.remaining);
  $('#dRemain').style.color = s.remaining < 0 ? '#c62828' : '#2E7D32';
  $('#dBar').style.width = Math.min(100, s.percent) + '%';
  $('#dBar').style.background = s.status === 'EXCEEDED' ? '#c62828' : s.status === 'NEARING' ? '#ef6c00' : '#2E7D32';
  $('#dPercent').textContent = s.percent.toFixed(0) + '% of budget used';
  $('#dMessage').textContent = s.message;
}

// ---------- add expense ----------
async function loadAdd() { $('#aDate').value = ymd(new Date()); }

$('#addForm').onsubmit = async e => {
  e.preventDefault();
  try {
    const s = await post('/api/expenses', {
      amount: $('#aAmount').value, category: $('#aCategory').value,
      description: $('#aDesc').value, date: $('#aDate').value
    });
    $('#aAmount').value = ''; $('#aDesc').value = '';
    popup(s.message, kindOf(s.status));
  } catch (err) {
    popup(err.message, 'warn', 'Invalid input');
  }
};

// ---------- history ----------
async function loadHistory() {
  const list = await api('/api/expenses');
  let total = 0;
  $('#hBody').innerHTML = list.map(x => {
    total += x.amount;
    return `<tr><td>${x.date}</td><td>${esc(x.category)}</td><td>${esc(x.description)}</td>
            <td class="r">${peso(x.amount)}</td>
            <td><button class="btn danger" data-id="${x.id}">Delete</button></td></tr>`;
  }).join('') || '<tr><td colspan="5" class="muted">No expenses yet.</td></tr>';
  $('#hTotal').textContent = 'All-time total: ' + peso(total);
  document.querySelectorAll('#hBody button').forEach(b => b.onclick = async () => {
    if (!confirm('Delete this expense?')) return;
    try { await post('/api/expenses/delete', { id: b.dataset.id }); loadHistory(); }
    catch (err) { popup(err.message, 'bad', 'Error'); }
  });
}

// ---------- budget ----------
async function loadBudget() {
  const s = await api('/api/summary');
  $('#bInfo').innerHTML = `Current budget: <b>${peso(s.budget)}</b><br>
    Spent this month: <b>${peso(s.spent)}</b><br>Remaining: <b>${peso(s.remaining)}</b>`;
}
$('#budgetForm').onsubmit = async e => {
  e.preventDefault();
  try {
    const s = await post('/api/budget', { amount: $('#bAmount').value });
    $('#bAmount').value = '';
    loadBudget();
    popup(s.message, kindOf(s.status));
  } catch (err) {
    popup(err.message, 'warn', 'Invalid input');
  }
};

// ---------- calendar ----------
const today = new Date();
const cal = { y: today.getFullYear(), m: today.getMonth(), sel: ymd(today) };

$('#cMode').onchange = () => loadCalendar();
$('#cPrev').onclick = () => moveMonth(-1);
$('#cNext').onclick = () => moveMonth(1);
function moveMonth(d) {
  const dt = new Date(cal.y, cal.m + d, 1);
  cal.y = dt.getFullYear(); cal.m = dt.getMonth();
  cal.sel = ymd(dt);
  loadCalendar().catch(e => popup(e.message, 'bad', 'Error'));
}

async function loadCalendar() {
  $('#cTitle').textContent = new Date(cal.y, cal.m, 1).toLocaleString('en-US', { month: 'long', year: 'numeric' });
  const totals = await api('/api/daily?month=' + monthKey(cal.y, cal.m));

  let html = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'].map(d => `<div class="dow">${d}</div>`).join('');
  const first = new Date(cal.y, cal.m, 1).getDay();
  const days = new Date(cal.y, cal.m + 1, 0).getDate();
  for (let i = 0; i < first; i++) html += '<div></div>';
  for (let d = 1; d <= days; d++) {
    const key = cal.y + '-' + pad(cal.m + 1) + '-' + pad(d);
    const t = totals[key];
    html += `<button class="day ${t ? 'has' : ''} ${key === cal.sel ? 'sel' : ''}" data-d="${key}">
             <b>${d}</b><small>${t ? peso(t) : '&nbsp;'}</small></button>`;
  }
  $('#cGrid').innerHTML = html;
  document.querySelectorAll('#cGrid .day').forEach(b => b.onclick = () => {
    cal.sel = b.dataset.d;
    loadCalendar().catch(e => popup(e.message, 'bad', 'Error'));
  });

  // summary for Day / Week / Month
  const [sy, sm, sd] = cal.sel.split('-').map(Number);
  const selDate = new Date(sy, sm - 1, sd);
  const mode = $('#cMode').value;
  let from, to, label;
  if (mode === 'Week') {
    const start = new Date(sy, sm - 1, sd - selDate.getDay());
    const end = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 6);
    from = ymd(start); to = ymd(end); label = `Week: ${from} to ${to}`;
  } else if (mode === 'Month') {
    from = ymd(new Date(sy, sm - 1, 1)); to = ymd(new Date(sy, sm, 0));
    label = selDate.toLocaleString('en-US', { month: 'long', year: 'numeric' });
  } else {
    from = to = cal.sel; label = 'Day: ' + cal.sel;
  }
  const [list, s] = await Promise.all([api(`/api/expenses?from=${from}&to=${to}`), api('/api/summary')]);
  const total = list.reduce((a, x) => a + x.amount, 0);
  $('#cSummary').innerHTML = `<p><b>${label}</b><br>Total spending: <b>${peso(total)}</b><br>
    Remaining monthly budget: <b>${peso(s.remaining)}</b></p>` +
    (list.length ? '<ul>' + list.map(x =>
      `<li>${x.date} · ${esc(x.category)}: ${esc(x.description)} (${peso(x.amount)})</li>`).join('') + '</ul>'
      : '<p class="muted">No expenses in this period.</p>');
  $('#cWitty').textContent = s.message;
}

// ---------- breakdown ----------
async function loadBreakdown() {
  const b = await api('/api/breakdown');
  $('#brBars').innerHTML = b.categories.map(c => `
    <div class="bar-row"><span class="name">${esc(c.name)}</span>
      <div class="track"><div class="fill" style="width:${c.percent}%"></div></div>
      <span class="val">${c.percent.toFixed(0)}% (${peso(c.total)})</span></div>`).join('');
  $('#brTip').textContent = b.suggestion;
}

// Live Clock
function updateClock() {
    const clock = document.getElementById("liveClock");

    if (clock) {
        clock.textContent = new Date().toLocaleTimeString("en-US", {
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit"
        });
    }
}

updateClock();
setInterval(updateClock, 1000);


// Light and Dark Mode Toggle
const themeToggle = document.getElementById("themeToggle");
const themeIcon = document.getElementById("themeIcon");

if (themeToggle && themeIcon) {
    themeToggle.addEventListener("click", function () {
        document.body.classList.toggle("dark-mode");

        const isDark = document.body.classList.contains("dark-mode");

        themeIcon.textContent = isDark ? "☾" : "☼";
    });
}