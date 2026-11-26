// ---------- helpers ----------
const $ = s => document.querySelector(s);

const peso = n => '₱' + Number(n).toLocaleString('en-PH', {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2
});

const esc = s => String(s).replace(/[&<>"']/g, c => ({
  '&': '&amp;',
  '<': '&lt;',
  '>': '&gt;',
  '"': '&quot;',
  "'": '&#39;'
}[c]));

const pad = n => String(n).padStart(2, '0');
const ymd = d => d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
const monthKey = (y, m) => y + '-' + pad(m + 1);
const cap = s => s ? s.charAt(0).toUpperCase() + s.slice(1).toLowerCase() : s;


// ---------- API ----------
async function api(path, opts) {
  const res = await fetch(path, opts);
  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.error || 'Something went wrong.');
  }

  return data;
}

const post = (path, obj) => api('/api/expenses' === path ? path : path, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  body: new URLSearchParams(obj)
});


// ---------- popup notifications ----------
function popup(message, kind, title) {
  let heading = title;

  if (!heading) {
    if (kind === 'bad' || kind === 'warn') {
      heading = 'Action Required!';
    } else {
      heading = 'Successfully Saved!';
    }
  }

  $('#pTitle').textContent = heading;
  $('#pMsg').textContent = message;
  $('#popup').className = kind || '';
  $('#popup').classList.remove('hidden');
}

$('#pClose').onclick = () => {
  $('#popup').classList.add('hidden');
};

const kindOf = st =>
  st === 'EXCEEDED' ? 'bad' :
  st === 'NEARING' ? 'warn' : '';


// ---------- cleared budget ----------
const CLEARED_KEY = 'gk_budget_cleared';

const NO_BUDGET_MSGS = [
  'Beh, mag-budget ka muna. Baka mas marami pang laman ang shopping cart mo kaysa sa wallet mo.',
  "Oy, budget muna bago spend."
];

const budgetCleared = () => localStorage.getItem(CLEARED_KEY) === '1';

function applyCleared(s) {
  if (!budgetCleared()) return s;

  return {
    ...s,
    budget: 0,
    remaining: 0,
    percent: 0,
    status: 'NONE',
    message: NO_BUDGET_MSGS[
      Math.floor(Math.random() * NO_BUDGET_MSGS.length)
    ]
  };
}

const getSummary = async () => applyCleared(
  await api('/api/summary')
);


// ---------- navigation ----------
const loaders = {
  dashboard: loadDashboard,
  add: loadAdd,
  history: loadHistory,
  budget: loadBudget,
  calendar: loadCalendar,
  breakdown: loadBreakdown
};

document.querySelectorAll('#sidebar button').forEach(b => {
  b.onclick = () => show(b.dataset.page);
});

function show(page) {
  document.querySelectorAll('.page').forEach(p => {
    p.classList.add('hidden');
  });

  const target = $('#page-' + page);

  if (!target || !loaders[page]) return;

  target.classList.remove('hidden');

  document.querySelectorAll('#sidebar button').forEach(b => {
    b.classList.toggle('active', b.dataset.page === page);
  });

  loaders[page]().catch(e => popup(e.message, 'bad', 'Error'));
}


// ---------- dashboard ----------
async function loadDashboard() {
  const name = cap(currentName());

  $('#dWelcome').textContent = name
    ? 'Welcome ' + name + ' to GastosKita'
    : 'Welcome to GastosKita';

  const s = await getSummary();

  $('#dBudget').textContent = peso(s.budget);
  $('#dSpent').textContent = peso(s.spent);
  $('#dRemain').textContent = peso(s.remaining);

  $('#dRemain').style.color =
    s.remaining < 0 ? '#c62828' : '#2E7D32';

  $('#dBar').style.width = Math.min(100, s.percent) + '%';

  $('#dBar').style.background =
    s.status === 'EXCEEDED' ? '#c62828' :
    s.status === 'NEARING' ? '#ef6c00' :
    '#2E7D32';

  $('#dPercent').textContent =
    s.status === 'NONE'
      ? 'No budget set yet'
      : s.percent.toFixed(0) + '% of budget used';

  $('#dMessage').textContent = s.message;
}


// ---------- add expense ----------
async function loadAdd() {
  $('#aDate').value = ymd(new Date());
}

$('#addForm').onsubmit = async e => {
  e.preventDefault();

  try {
    const s = applyCleared(await post('/api/expenses', {
      amount: $('#aAmount').value,
      category: $('#aCategory').value,
      description: $('#aDesc').value,
      date: $('#aDate').value
    }));

    $('#aAmount').value = '';
    $('#aDesc').value = '';

    popup(s.message, kindOf(s.status), 'Successfully Saved!');

  } catch (err) {
    popup(err.message, 'warn', 'Invalid input');
  }
};


// ---------- history ----------
let historyExpenses = [];

function renderHistory() {
  const search = $('#hSearch').value.trim().toLowerCase();
  const category = $('#hCategoryFilter').value;
  const sort = $('#hSort').value;

  // Calculate all-time total
  const total = historyExpenses.reduce(
    (sum, x) => sum + Number(x.amount || 0),
    0
  );

  // Calculate spending per category
  const categoryTotals = {};

  historyExpenses.forEach(x => {
    const name = x.category || 'Others';

    categoryTotals[name] =
      (categoryTotals[name] || 0) + Number(x.amount || 0);
  });

  // Find the category with the highest spending
  const topCategory = Object.entries(categoryTotals)
    .sort((a, b) => b[1] - a[1])[0];

  // Update summary cards
  $('#hStatTotal').textContent = peso(total);
  $('#hStatCount').textContent = historyExpenses.length;
  $('#hStatCategory').textContent = topCategory
    ? topCategory[0]
    : 'None';

  // Search and category filtering
  let filtered = historyExpenses.filter(x => {
    const description = String(x.description || '').toLowerCase();
    const expenseCategory = String(x.category || '');

    const matchesSearch =
      description.includes(search) ||
      expenseCategory.toLowerCase().includes(search);

    const matchesCategory =
      category === 'All' || expenseCategory === category;

    return matchesSearch && matchesCategory;
  });

  // Sorting
  filtered.sort((a, b) => {
    if (sort === 'newest') {
      return String(b.date).localeCompare(String(a.date));
    }

    if (sort === 'oldest') {
      return String(a.date).localeCompare(String(b.date));
    }

    if (sort === 'highest') {
      return Number(b.amount) - Number(a.amount);
    }

    if (sort === 'lowest') {
      return Number(a.amount) - Number(b.amount);
    }

    return 0;
  });

  // Category icons
  const icons = {
    Food: '🍔',
    Transportation: '🚌',
    Shopping: '🛍️',
    Entertainment: '🎮',
    Others: '📦'
  };

  // Display expenses
  $('#hBody').innerHTML = filtered.length
    ? filtered.map(x => {
        const categoryName = String(x.category || 'Others');
        const categoryClass = categoryName
          .toLowerCase()
          .replace(/[^a-z]/g, '');

        return `
          <tr>
            <td>${esc(x.date)}</td>

            <td>
              <span class="history-category ${esc(categoryClass)}">
                ${icons[categoryName] || '📦'}
                ${esc(categoryName)}
              </span>
            </td>

            <td>${esc(x.description)}</td>

            <td class="r">${peso(x.amount)}</td>

            <td>
              <button
                class="btn danger history-delete-btn"
                data-id="${esc(x.id)}"
                type="button"
              >
                Delete
              </button>
            </td>
          </tr>
        `;
      }).join('')
    : `
      <tr>
        <td colspan="5" class="history-empty">
          <span class="history-empty-icon">🧾</span>
          <strong>No expenses found</strong>
          <span>
            ${
              search || category !== 'All'
                ? 'Try changing your search or category filter.'
                : 'Your wallet is having a peaceful day. Add your first expense!'
            }
          </span>
        </td>
      </tr>
    `;

  // Update all-time total
  $('#hTotal').textContent = peso(total);
}

// Load expense records
async function loadHistory() {
  historyExpenses = await api('/api/expenses');

  renderHistory();
}

// Search listener
$('#hSearch').addEventListener('input', renderHistory);

// Category filter listener
$('#hCategoryFilter').addEventListener('change', renderHistory);

// Sorting listener
$('#hSort').addEventListener('change', renderHistory);

// Delete expense using event delegation
$('#hBody').addEventListener('click', async e => {
  const button = e.target.closest('.history-delete-btn');

  if (!button) return;

  if (!confirm('Delete this expense?')) return;

  try {
    await post('/api/expenses/delete', {
      id: button.dataset.id
    });

    historyExpenses = historyExpenses.filter(
      x => String(x.id) !== String(button.dataset.id)
    );

    renderHistory();

    popup(
      'Expense successfully deleted.',
      '',
      'Expense Deleted!'
    );

  } catch (err) {
    popup(err.message, 'bad', 'Error');
  }
});


// ---------- budget ----------
async function loadBudget() {
  const s = await getSummary();

  $('#bInfo').innerHTML = s.status === 'NONE'
    ? `Current budget: <b>Not set</b><br>
       Spent this month: <b>${peso(s.spent)}</b>`
    : `Current budget: <b>${peso(s.budget)}</b><br>
       Spent this month: <b>${peso(s.spent)}</b><br>
       Remaining: <b>${peso(s.remaining)}</b>`;
}

$('#budgetForm').onsubmit = async e => {
  e.preventDefault();

  try {
    const s = await post('/api/budget', {
      amount: $('#bAmount').value
    });

    localStorage.removeItem(CLEARED_KEY);
    $('#bAmount').value = '';

    await loadBudget();

    popup(s.message, kindOf(s.status), 'Successfully Saved!');

  } catch (err) {
    popup(err.message, 'warn', 'Invalid input');
  }
};


// ---------- clear budget ----------
$('#clearBudgetBtn').onclick = () => {
  if (!confirm('Clear your monthly budget?')) return;

  localStorage.setItem(CLEARED_KEY, '1');

  loadBudget().catch(e => popup(e.message, 'bad', 'Error'));

  const wittyMessages = [
    "Budget cleared! Your wallet just got a fresh start. Don't let your gastos take over again! 😂",
    "And just like that, your budget is gone! Time to give your wallet a new game plan. 💸",
    "Starting from zero? No worries! Even your wallet deserves a reset sometimes. Just remember to budget wisely! 😉",
    "Your budget has officially left the chat. Set a new one before your gastos go wild! 😭",
    "Fresh start unlocked! Your wallet is ready for another budgeting adventure. Good luck! 💚"
  ];

  const randomMessage = wittyMessages[
    Math.floor(Math.random() * wittyMessages.length)
  ];

  popup(
    randomMessage,
    '',
    'Budget Cleared! 💸'
  );
};


// ---------- calendar ----------
const today = new Date();

const cal = {
  y: today.getFullYear(),
  m: today.getMonth(),
  sel: ymd(today)
};

$('#cMode').onchange = () => {
  loadCalendar().catch(e => popup(e.message, 'bad', 'Error'));
};

$('#cPrev').onclick = () => moveMonth(-1);
$('#cNext').onclick = () => moveMonth(1);

function moveMonth(d) {
  const dt = new Date(cal.y, cal.m + d, 1);

  cal.y = dt.getFullYear();
  cal.m = dt.getMonth();
  cal.sel = ymd(dt);

  loadCalendar().catch(e => popup(e.message, 'bad', 'Error'));
}

async function loadCalendar() {
  $('#cTitle').textContent = new Date(
    cal.y,
    cal.m,
    1
  ).toLocaleString('en-US', {
    month: 'long',
    year: 'numeric'
  });

  const totals = await api(
    '/api/daily?month=' + monthKey(cal.y, cal.m)
  );

  // Weekday HTML
  let html = [
    'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'
  ].map(d => <div class="dow">${d}</div>).join('');

  const first = new Date(cal.y, cal.m, 1).getDay();
  const days = new Date(cal.y, cal.m + 1, 0).getDate();

  for (let i = 0; i < first; i++) {
    html += '<div></div>';
  }

  for (let d = 1; d <= days; d++) {
    const key =
      cal.y + '-' + pad(cal.m + 1) + '-' + pad(d);

    const t = totals[key];

    html += `
      <button class="day ${t ? 'has' : ''} ${key === cal.sel ? 'sel' : ''}"
              data-d="${key}">
        <b>${d}</b>
        <small>${t ? peso(t) : '&nbsp;'}</small>
      </button>
    `;
  }

  $('#cGrid').innerHTML = html;

  document.querySelectorAll('#cGrid .day').forEach(b => {
    b.onclick = () => {
      cal.sel = b.dataset.d;

      loadCalendar().catch(e => popup(e.message, 'bad', 'Error'));
    };
  });

  // Summary for Day / Week / Month
  const [sy, sm, sd] = cal.sel.split('-').map(Number);
  const selDate = new Date(sy, sm - 1, sd);
  const mode = $('#cMode').value;

  let from, to, label;

  if (mode === 'Week') {
    const start = new Date(
      sy,
      sm - 1,
      sd - selDate.getDay()
    );

    const end = new Date(
      start.getFullYear(),
      start.getMonth(),
      start.getDate() + 6
    );

    from = ymd(start);
    to = ymd(end);

    label = Week: ${from} to ${to};

  } else if (mode === 'Month') {
    from = ymd(new Date(sy, sm - 1, 1));
    to = ymd(new Date(sy, sm, 0));

    label = selDate.toLocaleString('en-US', {
      month: 'long',
      year: 'numeric'
    });

  } else {
    from = to = cal.sel;
    label = 'Day: ' + cal.sel;
  }

  const [list, s] = await Promise.all([
    api(/api/expenses?from=${from}&to=${to}),
    getSummary()
  ]);

  const total = list.reduce((a, x) => a + x.amount, 0);

  $('#cSummary').innerHTML = `
    <p>
      <b>${label}</b><br>
      Total spending: <b>${peso(total)}</b><br>
      Remaining monthly budget:
      <b>${s.status === 'NONE' ? 'No budget set' : peso(s.remaining)}</b>
    </p>
  ` + (
    list.length
      ? '<ul>' + list.map(x =>
          `<li>${x.date} · ${esc(x.category)}:
          ${esc(x.description)} (${peso(x.amount)})</li>`
        ).join('') + '</ul>'
      : '<p class="muted">No expenses in this period.</p>'
  );

  $('#cWitty').textContent = s.message;
}


// ---------- breakdown ----------
async function loadBreakdown() {
  const b = await api('/api/breakdown');

  // Display spending breakdown bars
  $('#brBars').innerHTML = b.categories.map(c => `
    <div class="bar-row">
      <span class="name">${esc(c.name)}</span>
      <div class="track">
        <div class="fill" style="width:${c.percent}%"></div>
      </div>
      <span class="val">
        ${Number(c.percent).toFixed(0)}% (${peso(c.total)})
      </span>
    </div>
  `).join('');

  // Witty jokes and practical advice per category
  const savingTips = {
    Food: [
      {
        joke: "Beh, hindi lahat ng cravings kailangang i-checkout! Your wallet deserves a break.",
        advice: "Try bringing food or snacks from home a few days a week. Plan your meals to avoid unnecessary food deliveries."
      },
      {
        joke: "Ang cravings mo, may sariling monthly subscription na yata!",
        advice: "Set a food allowance and stick to it. You can still enjoy your favorite food without overspending."
      },
      {
        joke: "Kain nang kain, pero ang wallet mo nagugutom na!",
        advice: "Prepare a simple meal plan and limit spontaneous food purchases."
      }
    ],

    Transportation: [
      {
        joke: "Parang may personal driver ka sa dami ng pamasahe mo, beh!",
        advice: "Combine errands in one trip and consider affordable transportation options when available."
      },
      {
        joke: "Bawat sakay, may kasamang goodbye sa budget!",
        advice: "Plan your trips ahead to avoid unnecessary rides and extra transportation expenses."
      },
      {
        joke: "Hindi lahat ng lakad kailangan may pamasahe!",
        advice: "For nearby destinations, consider walking when it is safe and practical."
      }
    ],

    Shopping: [
      {
        joke: "Add to cart ngayon, regret later. Familiar ba, beh?",
        advice: "Wait 24 hours before buying non-essential items. Ask yourself if you really need them."
      },
      {
        joke: "Your cart is full, but is your wallet emotionally prepared?",
        advice: "Make a shopping list and prioritize necessities before buying anything else."
      },
      {
        joke: "Beh, hindi lahat ng sale ay savings!",
        advice: "Compare prices and avoid impulse purchases. A discount is not a saving if you did not need the item."
      }
    ],

    Entertainment: [
      {
        joke: "Enjoy life, pero huwag naman pati emergency fund kasama sa gala!",
        advice: "Set a weekly entertainment allowance and explore free activities with friends."
      },
      {
        joke: "Main character ka sa gala, pero supporting character na lang ang budget!",
        advice: "Look for affordable activities and plan your outings ahead of time."
      },
      {
        joke: "Masaya ang gala, pero mas masaya kung may natirang pera!",
        advice: "Choose which activities are worth spending on and limit unnecessary expenses."
      }
    ],

    Others: [
      {
        joke: "Beh, saan napunta ang pera? Parang may magic trick na nangyari sa wallet mo!",
        advice: "Review your miscellaneous expenses and separate your needs from your wants."
      },
      {
        joke: "Miscellaneous lang dapat, hindi mystery spending!",
        advice: "Write down your extra purchases and set a weekly limit for unexpected expenses."
      },
      {
        joke: "Small gastos today, malaking plot twist sa wallet tomorrow!",
        advice: "Track even your small purchases. They can add up faster than you expect."
      }
    ]
  };

  // Find the category with the highest spending
  const highest = b.categories.reduce((max, current) =>
    Number(current.total) > Number(max.total) ? current : max,
    { total: 0, name: '', percent: 0 }
  );

  const tipContainer = $('#brTip');

  // Empty state
  if (!highest.name || Number(highest.total) <= 0) {
    tipContainer.innerHTML = `
      <div class="saving-message">
        <h3>Your wallet is waiting! 💸</h3>
        <p class="saving-joke">
          Wala pang gastos na recorded. Your wallet is currently living its peaceful era!
        </p>
        <div class="saving-advice">
          <strong>💡 Little tip:</strong>
          <p>
            Add your expenses first so GastosKita can help you
            understand where your money goes.
          </p>
        </div>
      </div>
    `;
    return;
  }

  // Select a random suggestion for the highest category
  const category = highest.name;
  const tips = savingTips[category] || [
    {
      joke: "Beh, your wallet is silently judging your spending decisions! 😭",
      advice: "Take a closer look at your expenses and find purchases you can cut back on. Your future self will thank you!"
    }
  ];

  const selectedTip = tips[
    Math.floor(Math.random() * tips.length)
  ];

  // Display category, joke, and advice separately
  tipContainer.innerHTML = `
    <div class="saving-message">
      <div class="saving-category">
        <span>HIGHEST SPENDING CATEGORY</span>
        <strong>
          ${esc(category)} · ${Number(highest.percent).toFixed(0)}%
        </strong>
      </div>

      <h3>Your wallet has something to say! 💸</h3>

      <p class="saving-joke">
        "${esc(selectedTip.joke)}"
      </p>

      <div class="saving-advice">
        <strong>💡 Little tip:</strong>
        <p>${esc(selectedTip.advice)}</p>
      </div>
    </div>
  `;
}


// ---------- login ----------
const USERS_KEY = 'gk_users';
const SESSION_KEY = 'gk_session';
const NAME_KEY = 'gk_name';

const MIN_PASSWORD = 5;

let signupMode = false;

const currentName = () =>
  localStorage.getItem(NAME_KEY) ||
  localStorage.getItem(SESSION_KEY) ||
  '';

const loginErr = m => {
  $('#lError').textContent = m;
};

async function sha256(text) {
  const buf = await crypto.subtle.digest(
    'SHA-256',
    new TextEncoder().encode(text)
  );

  return Array.from(new Uint8Array(buf))
    .map(b => b.toString(16).padStart(2, '0'))
    .join('');
}

function getUsers() {
  try {
    return JSON.parse(localStorage.getItem(USERS_KEY)) || {};
  } catch (e) {
    return {};
  }
}

function enterApp() {
  $('#login').classList.add('hidden');
  $('#welcome').classList.add('hidden');

  const name = cap(currentName());

  $('#uwTitle').textContent =
    'Welcome ' + (name ? name + ' ' : '') + 'to GastosKita website!';

  $('#userWelcome').classList.remove('hidden');
}

$('#uwBtn').onclick = () => {
  $('#userWelcome').classList.add('hidden');
  $('#app').classList.remove('hidden');

  show('dashboard');
};

function setMode(signup) {
  signupMode = signup;

  $('#loginTitle').textContent = signup
    ? 'Create your account'
    : 'Log in to GastosKita';

  $('#lSubmit').textContent = signup ? 'Sign up' : 'Log in';

  $('#lSwitchText').textContent = signup
    ? 'Already have an account?'
    : 'No account yet?';

  $('#lSwitch').textContent = signup ? 'Log in' : 'Sign up';

  $('#lConfirmWrap').classList.toggle('hidden', !signup);
  $('#lConfirm').required = signup;

  loginErr('');
}

$('#startBtn').onclick = () => {
  $('#welcome').classList.add('hidden');

  if (localStorage.getItem(SESSION_KEY)) {
    enterApp();
  } else {
    $('#login').classList.remove('hidden');
  }
};

$('#lSwitch').onclick = e => {
  e.preventDefault();
  setMode(!signupMode);
};

$('#loginForm').onsubmit = async e => {
  e.preventDefault();

  const raw = $('#lUser').value.trim();
  const user = raw.toLowerCase();
  const pass = $('#lPass').value;

  const users = getUsers();

  let display = raw;

  try {
    if (signupMode) {

      if (user.length < 3) {
        return loginErr('Username must be at least 3 characters.');
      }

      if (pass.length < MIN_PASSWORD) {
        return loginErr(
          'Password must be at least ' + MIN_PASSWORD + ' characters.'
        );
      }

      if (pass !== $('#lConfirm').value) {
        return loginErr('Passwords do not match.');
      }

      if (users[user]) {
        return loginErr('That username is already taken.');
      }

      users[user] = {
        h: await sha256(pass),
        n: raw
      };

      localStorage.setItem(USERS_KEY, JSON.stringify(users));

    } else {

      const rec = users[user];

      const hash = rec && (
        typeof rec === 'string' ? rec : rec.h
      );

      if (!hash || hash !== await sha256(pass)) {
        return loginErr('Wrong username or password.');
      }

      display = (rec && rec.n) || user;
    }

    localStorage.setItem(SESSION_KEY, user);
    localStorage.setItem(NAME_KEY, display);

    $('#loginForm').reset();

    enterApp();

  } catch (ex) {
    loginErr('Something went wrong: ' + ex.message);
  }
};


// ---------- logout ----------
$('#logoutBtn').onclick = () => {
  localStorage.removeItem(SESSION_KEY);
  localStorage.removeItem(NAME_KEY);

  $('#userWelcome').classList.add('hidden');
  $('#app').classList.add('hidden');

  setMode(false);

  $('#login').classList.remove('hidden');
};


// ---------- live clock ----------
function updateClock() {
  const clock = document.getElementById('liveClock');

  if (clock) {
    clock.textContent = new Date().toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  }
}

updateClock();
setInterval(updateClock, 1000);


// ---------- light and dark mode ----------
const themeToggle = document.getElementById('themeToggle');
const themeIcon = document.getElementById('themeIcon');

if (themeToggle && themeIcon) {
  themeToggle.addEventListener('click', function () {
    document.body.classList.toggle('dark-mode');

    const isDark =
      document.body.classList.contains('dark-mode');

    themeIcon.textContent = isDark ? '☾' : '☼';
  });
}
