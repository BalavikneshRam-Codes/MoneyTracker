// --- CALCULATOR STATE VARIABLES ---
let currentVal = '0';
let previousVal = null;
let currentOperator = null;
let resetDisplayNext = false;

let display = $('#calc-display');

// 1. Helper Function to do the math safely
function calculateResult(a, b, operator) {
    let num1 = parseFloat(a);
    let num2 = parseFloat(b);
    if (isNaN(num1) || isNaN(num2)) return '0';

    let result = 0;
    if (operator === '+') result = num1 + num2;
    else if (operator === '-') result = num1 - num2;
    else if (operator === '×') result = num1 * num2;
    else if (operator === '÷') result = num1 / num2;

    return parseFloat(result.toFixed(10)).toString();
}

// 2. Number & Decimal Clicks (Fixed Double-Click Bug)
$('.btn-num').off('click').on('click', function() {
    let val = $(this).text().trim();

    if (resetDisplayNext) {
        currentVal = (val === '.') ? '0.' : val;
        resetDisplayNext = false;
    } else {
        if (val === '.' && currentVal.includes('.')) return;

        if (currentVal === '0' && val !== '.') {
            currentVal = val;
        } else {
            currentVal += val;
        }
    }
    display.text(currentVal);
    $('#calc-display').removeClass('error-highlight');
});

// 3. Operator Clicks (Fixed Double-Click Bug)
$('.btn-op').off('click').on('click', function() {
    let op = $(this).text().trim();

    if (op === '=') {
        if (currentOperator !== null && previousVal !== null) {
            currentVal = calculateResult(previousVal, currentVal, currentOperator);
            display.text(currentVal);

            previousVal = null;
            currentOperator = null;
            resetDisplayNext = true;
        }
        return;
    }

    if (currentOperator !== null && previousVal !== null && !resetDisplayNext) {
        currentVal = calculateResult(previousVal, currentVal, currentOperator);
        display.text(currentVal);
    }

    previousVal = currentVal;
    currentOperator = op;
    resetDisplayNext = true;
});

// 4. Backspace Click (Fixed Double-Click Bug)
$('#btn-backspace').off('click').on('click', function() {
    if (resetDisplayNext) {
        currentVal = '0';
        display.text(currentVal);
        return;
    }

    if (currentVal.length > 1) {
        currentVal = currentVal.slice(0, -1);
    } else {
        currentVal = '0';
    }
    display.text(currentVal);
});