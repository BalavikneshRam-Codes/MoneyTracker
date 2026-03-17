/*<![CDATA[*/

// ── CONFIG ──────────────────────────────────────────────────────────────────
const API_URL = 'fetchNotes'; // Replace with actual endpoint
const USER_ID = $("#loggedUserId").val()/*[[${session.userId ?: 1}]]*/ ;
let currentDate = new Date();

// ── CATEGORY CONFIG ──────────────────────────────────────────────────────────
const CAT_CONFIG = {
    'Food':          { icon: 'bi-egg-fried',       css: 'food' },
    'Transportation':{ icon: 'bi-bus-front',        css: 'transport' },
    'Shopping':      { icon: 'bi-cart3',            css: 'shopping' },
    'Home':          { icon: 'bi-house',            css: 'home' },
    'Social':        { icon: 'bi-people',           css: 'social' },
    'Salary':        { icon: 'bi-wallet2',          css: 'salary' },
    'Entertainment': { icon: 'bi-music-note-beamed',css: 'entertainment' },
    'Health':        { icon: 'bi-heart-pulse',      css: 'health' },
};

function getCatConfig(name) {
    return CAT_CONFIG[name] || { icon: 'bi-tag', css: 'default' };
}

// ── MONTH NAVIGATION ─────────────────────────────────────────────────────────

function updateMonthLabel() {
    const opts = { month: 'long', year: 'numeric' };
    $('#currentMonthLabel').text(currentDate.toLocaleDateString('en-US', opts));
}

function getMonthRange() {
    const y = currentDate.getFullYear();
    const m = currentDate.getMonth();

    const from = new Date(y, m, 1);
    const to = new Date(y, m + 1, 0); // Always uses exact month end

    return {
        fromDate: formatApiDate(from),
        toDate: formatApiDate(to)
    };
}

function setDefaultDateInputs() {
    const d = (typeof currentDate !== 'undefined') ? currentDate : new Date();
    const y = d.getFullYear();
    const m = d.getMonth();
    const firstDay = new Date(y, m, 1);
    const lastDay = new Date(y, m + 1, 0);

    const formatForInput = (dateObj) => {
        const yyyy = dateObj.getFullYear();
        const mm = String(dateObj.getMonth() + 1).padStart(2, '0');
        const dd = String(dateObj.getDate()).padStart(2, '0');
        return `${yyyy}-${mm}-${dd}`;
    };

    $('#fromDate').val(formatForInput(firstDay));
    $('#toDate').val(formatForInput(lastDay));
}

function formatApiDate(d) {
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${mm} ${dd},${d.getFullYear()}`;
}

$('#prevMonth').on('click', function () {
    currentDate.setMonth(currentDate.getMonth() - 1);
    updateMonthLabel();
    updateDateRangeLabel();
    setDefaultDateInputs();
    fetchTransactions();
});

$('#nextMonth').on('click', function () {
    currentDate.setMonth(currentDate.getMonth() + 1);
    updateMonthLabel();
    updateDateRangeLabel();
    setDefaultDateInputs();
    fetchTransactions();
});

// ── FETCH ─────────────────────────────────────────────────────────────────────
function fetchTransactions() {
    const fromInput = $('#fromDate').val(); // Format: YYYY-MM-DD
    const toInput = $('#toDate').val();

    const fromDateObj = new Date(fromInput + 'T00:00:00');
    const toDateObj = new Date(toInput + 'T00:00:00');

    const payload = {
        userId:   USER_ID,
        fromDate: formatApiDate(fromDateObj),
        toDate:   formatApiDate(toDateObj)
    };

    $('#loadingOverlay').addClass('show');
    $('#transactionsList').empty();
    $('#emptyState').hide();

    $.ajax({
        url:         API_URL,
        type:        'POST',
        contentType: 'application/json',
        data:        JSON.stringify(payload),
        success: function (res) {
            $('#loadingOverlay').removeClass('show');
            if (res.status === 'SUCCESS') {
                renderTransactions(res.userNotesVOS || []);
            } else {
                showError(res.errorMessage || 'Failed to load transactions.');
            }
        },
        error: function (xhr) {
            $('#loadingOverlay').removeClass('show');
            showError('Network error. Using demo data.');
        }
    });
}

// ── RENDER ────────────────────────────────────────────────────────────────────
function renderTransactions(userNotesVOS) {
    const $list = $('#transactionsList');
    $list.empty();

    if (!userNotesVOS || userNotesVOS.length === 0) {
        $('#emptyState').css('display', 'flex');
        updateSummary([], []);
        return;
    }

    let allIncome = [], allExpense = [];

    userNotesVOS.forEach(function (dayGroup, idx) {
        const notes = dayGroup.noteVOS || [];
        const dayIncome  = notes.filter(n => n.transactionVO && n.transactionVO.transactionKeyName === 'INCOME');
        const dayExpense = notes.filter(n => n.transactionVO && n.transactionVO.transactionKeyName !== 'INCOME');

        allIncome  = allIncome.concat(dayIncome);
        allExpense = allExpense.concat(dayExpense);

        const dayNet = dayIncome.reduce((s, n) => s + n.amount, 0)
                     - dayExpense.reduce((s, n) => s + n.amount, 0);

        const $group = $('<div class="day-group"></div>').css('animation-delay', (idx * 0.05) + 's');

        const $header = $(`
            <div class="day-header">
                <span class="day-label">${escapeHtml(dayGroup.noteDate)}</span>
                <div class="day-line"></div>
                <span class="day-total">${formatAmount(dayNet)}</span>
            </div>
        `);

        // Added standard DataTables header elements (<thead>) so columns can be clicked/sorted
        const $table = $('<table class="txn-table w-100"></table>');
        const $thead = $(`
            <thead style="display:none;">
                <tr>
                    <th>Icon</th>
                    <th>Category</th>
                    <th>Note</th>
                    <th>Type</th>
                    <th>Date/Time</th>
                    <th>Amount</th>
                </tr>
            </thead>
        `);
        const $tbody = $('<tbody></tbody>');

        notes.forEach(function (note) {
            const cat   = note.categoryVO   || {};
            const acc   = note.accountVO    || {};
            const txn   = note.transactionVO || {};
            const isIncome = txn.transactionKeyName === 'INCOME';
            const cfg   = getCatConfig(cat.categoryName || '');

            const badgeClass  = isIncome ? 'badge-income' : (txn.transactionKeyName === 'TRANSFER' ? 'badge-transfer' : 'badge-expense');
            const amountClass = isIncome ? 'income' : 'expense';
            const amountSign  = isIncome ? '+' : '−';

            const $row = $(`
                <tr class="txn-row">
                    <td style="width:56px">
                        <div class="cat-icon ${cfg.css}">
                            <i class="bi ${cfg.icon}"></i>
                        </div>
                    </td>
                    <td style="min-width:50px">
                        <div class="txn-name">${escapeHtml(cat.categoryName || 'Unknown')}</div>
                        <div class="txn-meta">
                            <span class="txn-account">
                                <i class="bi bi-${acc.accountName === 'Cash' ? 'cash-coin' : 'credit-card'}" style="font-size:10px"></i>
                                ${escapeHtml(acc.accountName || '—')}
                            </span>
                        </div>
                    </td>
                    <td>
                        <div class="txn-note">${escapeHtml(note.notesMessage || '')}</div>
                    </td>
                    <td style="width:110px; text-align:center">
                        <span class="txn-type-badge ${badgeClass}">${escapeHtml(txn.transactionDisplayName || txn.transactionKeyName || '—')}</span>
                    </td>
                    <td style="width: 180px;text-align:center">
                        <div class="txn-time" style="margin-bottom: 8px;">${escapeHtml(note.recordTime || '')}</div>
                        <div style="font-size:11px;color:var(--text-muted);">${escapeHtml(note.recordData || '')}</div>
                    </td>
                    <td style="width:140px; text-align:right;">
                        <div class="txn-amount ${amountClass}">
                            ${amountSign}₹${Math.abs(note.amount).toFixed(2)}
                        </div>
                    </td>
                </tr>
            `);

            $tbody.append($row);
        });

        $table.append($thead).append($tbody);
        $group.append($header).append($table);
        $list.append($group);

        // --- DATA TABLES INITIALIZATION ---
        // Initializes DataTables on this specific day's table
        $table.DataTable({
            paging: false,       // No pagination needed for daily groupings
            searching: false,    // No individual search bars per day
            info: false,         // Hides "Showing 1 to X entries"
            ordering: true,      // Allows you to click invisible headers to sort (or trigger via JS)
            autoWidth: true      // Prevents table jumping
        });
    });

    updateSummary(allIncome, allExpense);
}

function updateSummary(income, expense) {
    const totalIncome  = income.reduce((s, n)  => s + n.amount, 0);
    const totalExpense = expense.reduce((s, n) => s + n.amount, 0);
    const net          = totalIncome - totalExpense;

    $('#summaryIncome').text('₹' + totalIncome.toFixed(2));
    $('#summaryExpense').text('₹' + totalExpense.toFixed(2));
    $('#summaryTotal').text((net >= 0 ? '+' : '−') + '₹' + Math.abs(net).toFixed(2));
    $('#summaryIncomeCount').text(income.length + ' transaction' + (income.length !== 1 ? 's' : ''));
    $('#summaryExpenseCount').text(expense.length + ' transaction' + (expense.length !== 1 ? 's' : ''));
}

function formatAmount(val) {
    return (val >= 0 ? '+' : '−') + '₹' + Math.abs(val).toFixed(2);
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function showError(msg) {
    $('#transactionsList').html(`
        <div style="text-align:center;padding:40px;color:var(--accent-red)">
            <i class="bi bi-exclamation-triangle" style="font-size:36px;display:block;margin-bottom:12px"></i>
            <p>${escapeHtml(msg)}</p>
        </div>
    `);
}

// ── UPDATE DATE RANGE LABEL ───────────────────────────────────────────────────
function updateDateRangeLabel() {
    const y = currentDate.getFullYear();
    const m = currentDate.getMonth();

    const from = new Date(y, m, 1);
    const to = new Date(y, m + 1, 0); // Removed the 'today' cutoff so it matches input

    const fmt = d => d.toLocaleDateString('en-IN', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });

    $('#dateRangeText').text(fmt(from) + ' – ' + fmt(to));
}

// ── INIT ──────────────────────────────────────────────────────────────────────
$(function () {
    updateMonthLabel();
    setDefaultDateInputs();
    updateDateRangeLabel();
    fetchTransactions();

    // Fetch button
    $('#btnFetch').on('click', fetchTransactions);

    // Add button (stub)
    $('#btnAdd').on('click', function () {
        alert('Add Transaction modal — wire to your Spring controller here.');
    });
});

/*]]>*/