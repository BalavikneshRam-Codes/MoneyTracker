$(function () {

     /* ══════════════════════════════════════════
        Helpers
     ══════════════════════════════════════════ */
     const $email    = $('#username');
     const $pw       = $('#password');
     const $emailErr = $('#emailError');
     const $pwErr    = $('#pwError');

     function showFieldError($field, $err) {
         $field.addClass('is-invalid');
         $err.addClass('show-error');
     }
     function clearFieldError($field, $err) {
         $field.removeClass('is-invalid');
         $err.removeClass('show-error');
     }
     function isValidEmail(val) {
         return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
     }

     /* ── Show / hide API-level alert banner ── */
     function showApiAlert(msg, isSuccess) {
         const $alert = $('#apiAlert');
         $('#apiAlertMsg').text(msg);
         $alert
             .removeClass('alert-custom alert-success-custom')
             .addClass(isSuccess ? 'alert-success-custom' : 'alert-custom')
             .fadeIn(200);
     }
     function hideApiAlert() { $('#apiAlert').fadeOut(150); }

     /* ── Shake the button on client-side error ── */
     function shakeBtn() {
         $('#submitBtn').css('animation', 'none');
         setTimeout(function () {
             $('#submitBtn').css('animation', 'shake 0.4s ease');
             setTimeout(() => $('#submitBtn').css('animation', ''), 420);
         }, 10);
     }

     /* ══════════════════════════════════════════
        Toggle password visibility
     ══════════════════════════════════════════ */
     $('#togglePw').on('click', function () {
         const isHidden = $pw.attr('type') === 'password';
         $pw.attr('type', isHidden ? 'text' : 'password');
         $('#pwIcon')
             .toggleClass('bi-eye',      !isHidden)
             .toggleClass('bi-eye-slash', isHidden);
     });

     /* ══════════════════════════════════════════
        Live field validation
     ══════════════════════════════════════════ */
     $email
         .on('blur', function () {
             const val = $(this).val().trim();
             !val || !isValidEmail(val)
                 ? showFieldError($email, $emailErr)
                 : clearFieldError($email, $emailErr);
         })
         .on('input', function () {
             if ($(this).hasClass('is-invalid') && isValidEmail($(this).val().trim())) {
                 clearFieldError($email, $emailErr);
             }
         });

     $pw
         .on('blur', function () {
             $(this).val().trim() === ''
                 ? showFieldError($pw, $pwErr)
                 : clearFieldError($pw, $pwErr);
         })
         .on('input', function () {
             if ($(this).hasClass('is-invalid') && $(this).val().trim() !== '') {
                 clearFieldError($pw, $pwErr);
             }
         });

     /* ══════════════════════════════════════════
        Client-side validation before API call
     ══════════════════════════════════════════ */
     function validateForm() {
         let ok = true;
         const emailVal = $email.val().trim();
         if (!emailVal || !isValidEmail(emailVal)) { showFieldError($email, $emailErr); ok = false; }
         if (!$pw.val().trim())                     { showFieldError($pw, $pwErr);       ok = false; }
         if (!ok) shakeBtn();
         return ok;
     }

     /* ══════════════════════════════════════════
        Login API call  (jQuery $.ajax)
     ══════════════════════════════════════════ */
     $('#submitBtn').on('click', function () {
         hideApiAlert();
         if (!validateForm()) return;

         const $btn = $(this);
         $btn.addClass('loading').prop('disabled', true);

         const payload = {
             username:   $email.val().trim(),
             password:   $pw.val().trim(),
         };

         $.ajax({
             url:         '/api/auth/login',   // ← change to your actual endpoint
             method:      'POST',
             contentType: 'application/json',
             data:        JSON.stringify(payload),

             success: function (res) {
                 showApiAlert('Login successful! Redirecting…', true);
                 if (res.token) {
                     localStorage.setItem('authToken', res.token);
                 }
                 setTimeout(function () {
                     window.location.href = res.redirectUrl || '/dashboard';
                 }, 800);
             },
             error: function (xhr) {
                 $btn.removeClass('loading').prop('disabled', false);
                 let msg = 'Invalid email or password. Please try again.';
                 try {
                     const body = JSON.parse(xhr.responseText);
                     if (body.message) msg = body.message;
                 } catch (_) {}
                 if (xhr.status === 401) msg = 'Invalid email or password.';
                 if (xhr.status === 403) msg = 'Your account has been locked. Please contact support.';
                 if (xhr.status === 429) msg = 'Too many attempts. Please wait a moment and try again.';
                 if (xhr.status === 0)   msg = 'Network error — please check your connection.';
                 showApiAlert(msg, false);
                 shakeBtn();
             }
         });
     });

     /* ── Also allow Enter key to trigger login ── */
     $('#loginForm').on('keydown', function (e) {
         if (e.key === 'Enter') $('#submitBtn').trigger('click');
     });

 });