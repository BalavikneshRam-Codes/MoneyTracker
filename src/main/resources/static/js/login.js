$(function () {

    /* ── Toggle password visibility ── */
    $('#togglePw').on('click', function () {
        const $input = $('#password');
        const $icon  = $('#pwIcon');
        const isHidden = $input.attr('type') === 'password';
        $input.attr('type', isHidden ? 'text' : 'password');
        $icon.toggleClass('bi-eye', !isHidden).toggleClass('bi-eye-slash', isHidden);
    });

    /* ── Live validation helpers ── */
    function showError($field, $err) {
        $field.addClass('is-invalid');
        $err.addClass('show-error');
    }
    function clearError($field, $err) {
        $field.removeClass('is-invalid');
        $err.removeClass('show-error');
    }

    const $email    = $('#username');
    const $pw       = $('#password');
    const $emailErr = $('#emailError');
    const $pwErr    = $('#pwError');

    $email.on('blur', function () {
        const val = $(this).val().trim();
        const valid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
        valid || val === '' ? clearError($email, $emailErr) : showError($email, $emailErr);
        if (val === '') showError($email, $emailErr);
    }).on('input', function () {
        if ($(this).hasClass('is-invalid')) {
            const val = $(this).val().trim();
            if (/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val)) clearError($email, $emailErr);
        }
    });

    $pw.on('blur', function () {
        $(this).val().trim() === ''
            ? showError($pw, $pwErr)
            : clearError($pw, $pwErr);
    }).on('input', function () {
        if ($(this).hasClass('is-invalid') && $(this).val().trim() !== '') {
            clearError($pw, $pwErr);
        }
    });

    /* ── Form submit: validation + loading state ── */
    $('#loginForm').on('submit', function (e) {
        let valid = true;

        const emailVal = $email.val().trim();
        if (!emailVal || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailVal)) {
            showError($email, $emailErr);
            valid = false;
        }

        if (!$pw.val().trim()) {
            showError($pw, $pwErr);
            valid = false;
        }

        if (!valid) {
            e.preventDefault();
            // Shake animation
            $('#loginForm').css('animation', 'none');
            setTimeout(function () {
                $('#submitBtn').css({
                    animation: 'shake 0.4s ease',
                });
                setTimeout(() => $('#submitBtn').css('animation', ''), 400);
            }, 10);
            return;
        }

        // Loading state
        $('#submitBtn').addClass('loading').prop('disabled', true);
    });

    /* ── Input focus: remove server-side error style ── */
    $('.form-control-custom').on('focus', function () {
        // Allow re-validation on re-focus
    });

});