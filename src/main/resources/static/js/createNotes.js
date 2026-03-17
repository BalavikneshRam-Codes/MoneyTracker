let isManuallyChanged = false;
let datePicker;
let timePicker;
let selectedAccountId = null;
let selectedCategoryId = null;
$(document).ready(function() {
    let datePicker = flatpickr("#current-date", {
            dateFormat: "M d, Y",
            defaultDate: new Date(),
            onChange: function(selectedDates, dateStr, instance) {
                isManuallyChanged = true;
            }
    });
    let timePicker = flatpickr("#current-time", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "h:i K",
        defaultDate: new Date(),
        onChange: function(selectedDates, dateStr, instance) {
            isManuallyChanged = true;
        }
    });
    updateDateTime();
    setInterval(updateDateTime, 60000);
    let display = $('#calc-display');
    fetchAccountAndCategoryData();
 });
function updateDateTime() {
    if (isManuallyChanged) return;
    const now = new Date();
     if(datePicker && timePicker) {
         datePicker.setDate(now, false);
         timePicker.setDate(now, false);
     }
}

 function fetchAccountAndCategoryData() {
     let requestData = {}
     $.ajax({
         url: '/fetchNotesInfo',
         type: 'POST',
         contentType: 'application/json',
         data: JSON.stringify(requestData),
         success: fetchNotesInfoSuccessHandler,
         error: function(xhr, status, error) {
             console.error("Failed to fetch notes info:", error);
         }
     });
 }

 function fetchNotesInfoSuccessHandler(response) {
     if(response && response.status === 'SUCCESS') {
         renderTransactions(response.transactionVOS);
         renderAccounts(response.accountVOS);
         renderCategories(response.categoryVOS);
     }
 }
function renderTransactions(transactions){
    let container = $("#transaction-tabs");
    container.empty();
    transactions.forEach(function(tx, index){
        let isActive = tx.transactionKeyName === "EXPENSE";
        let activeClass = isActive ? "active" : "";
        let iconHtml = isActive ? '<i class="fas fa-check-circle me-1 check-icon"></i> ' : '';
        let html = `
            <div class="nav-item ${activeClass}" data-key="${tx.transactionKeyName}">
                ${iconHtml}${tx.transactionDisplayName}
            </div>
        `;
        container.append(html);
        if (index < transactions.length - 1) {
            container.append(`<div class="nav-divider"></div>`);
        }
    });
}
function renderAccounts(accounts){
    let menu = $("#account-dropdown-menu");
    menu.empty();
    accounts.forEach(function(acc){
        let balanceVal = acc.balance ? parseFloat(acc.balance) : 0;
        let formattedBalance = "₹" + balanceVal.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        let item = `
            <li>
                <a class="dropdown-item account-item d-flex justify-content-between align-items-center"
                   href="#"
                   data-id="${acc.accountId}"
                   data-name="${acc.accountName}">
                   <span class="fw-medium">${acc.accountName}</span>
                   <span style="color: #8bd29b;">${formattedBalance}</span>
                </a>
            </li>
        `;
        menu.append(item);
    });
}
$(document).on("click", ".account-item", function(e) {
    e.preventDefault();
    let name = $(this).data("name");
    selectedAccountId = $(this).data("id"); // <-- SAVE THE ID
    $("#selected-account").text(name);
    $('#account-selector').removeClass('error-highlight');// (or #selected-account-text depending on your HTML)
});
function renderCategories(categories){
    let list = $("#category-list");
    list.empty();

    categories.forEach(function(cat){
        let defaultImage = "https://placehold.co/100x100/666353/dfd4a6?text=No+Img";
        let imgSrc = (cat.imageKey && cat.imageKey.trim() !== "") ? cat.imageKey : defaultImage;
        let item = `
            <li>
                <a class="dropdown-item category-item d-flex align-items-center"
                   href="#"
                   data-id="${cat.categoryId}"
                   data-name="${cat.categoryName}">
                   <img src="${imgSrc}"
                        alt="${cat.categoryName}"
                        class="me-2"
                        style="width: 28px; height: 28px; object-fit: cover; border-radius: 50%;"
                        onerror="this.onerror=null; this.src='${defaultImage}';">

                   <span class="category-name fw-medium">${cat.categoryName}</span>
                </a>
            </li>
        `;
        list.append(item);
    });
}
$(document).on("click", ".category-item", function(e) {
    e.preventDefault();
    let name = $(this).data("name");
    selectedCategoryId = $(this).data("id"); // <-- SAVE THE ID
    $("#selected-category").text(name);
    $('#category-selector').removeClass('error-highlight');
});
 $(document).on('click', '#transaction-tabs .nav-item', function() {
     $('#transaction-tabs .nav-item').removeClass('active');
     $('#transaction-tabs .check-icon').remove();
     $(this).addClass('active');
     $(this).prepend('<i class="fas fa-check-circle me-1 check-icon"></i> ');
     let selectedTransactionType = $(this).data('key');
     console.log("User switched tab to: " + selectedTransactionType);
 });
// Listen for clicks on the SAVE button
$('.header-save').off('click').on('click', function() {

    let amount = parseFloat($('#calc-display').text());
    let noteMessage = $('#note-message').val();
    let recordDate = $('#current-date').val();
    let recordTime = $('#current-time').val();
    let transactionName = $('#transaction-tabs .nav-item.active').data('key');

    // --- 1. Reset previous error highlights ---
    $('#account-selector, #category-selector, #calc-display').removeClass('error-highlight');

    // --- 2. Visual Validation ---
    let isValid = true;

    if (!selectedAccountId) {
        $('#account-selector').addClass('error-highlight');
        isValid = false;
    }
    if (!selectedCategoryId) {
        $('#category-selector').addClass('error-highlight');
        isValid = false;
    }
    if (amount === 0 || isNaN(amount)) {
        $('#calc-display').addClass('error-highlight');
        isValid = false;
    }

    // Stop execution if anything is invalid
    if (!isValid) return;

    // --- 3. Build Payload ---
    let noteReqVO = {
        userId:$("#loggedUserId").val()
        noteMessage: noteMessage,
        recordDate: recordDate,
        recordTime: recordTime,
        transactionName: transactionName,
        categoryId: selectedCategoryId,
        accountId: selectedAccountId,
        amount: amount
    };

    // --- 4. Update UI to show loading state ---
    let saveBtn = $(this);
    let originalHtml = saveBtn.html();
    saveBtn.html('<i class="fas fa-spinner fa-spin me-1"></i> SAVING...');
    saveBtn.css('pointer-events', 'none'); // Prevent double clicks

    // --- 5. AJAX Call ---
    $.ajax({
        url: '/createOrUpdateNote',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(noteReqVO),
        success: function(response) {
            window.location.href = "/";
        },
        error: function(xhr, status, error) {
            console.error("Failed to save note:", error);
            saveBtn.html('<i class="fas fa-exclamation-triangle me-1 text-danger"></i> ERROR');
            setTimeout(() => {
                saveBtn.html(originalHtml);
                saveBtn.css('pointer-events', 'auto');
            }, 3000);
        }
    });
});