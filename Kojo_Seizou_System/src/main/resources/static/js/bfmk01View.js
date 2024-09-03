// ページがロードされたところで実行
document.addEventListener('DOMContentLoaded', function() {
    // 変数宣言
    let guest = document.getElementById("auth_div_guest");
    let general = document.getElementById("auth_div_general");
    let manager = document.getElementById("auth_div_manager");

    // 【権限区分】の「ゲスト」にチェックがある時、「一般」と「管理者」はチェックを外す
    document.getElementById('auth_div_guest').addEventListener('click', function(e) {
        let auth_div = document.getElementById("auth_div_guest");
        unCheck(auth_div, guest, general, manager);
    }, false);

    // 【権限区分】の「一般」にチェックがある時、「ゲスト」と「管理者」はチェックを外す
    document.getElementById('auth_div_general').addEventListener('click', function(e) {
        let auth_div = document.getElementById("auth_div_general");
        unCheck(auth_div, guest, general, manager);
    }, false);

    // 【権限区分】の「管理者」にチェックがある時、「ゲスト」と「一般」はチェックを外す
    document.getElementById('auth_div_manager').addEventListener('click', function(e) {
        let auth_div = document.getElementById("auth_div_manager");
        unCheck(auth_div, guest, general, manager);
    }, false);

    // 権限区分「ゲスト」のチェック有無で、参照・操作権限の有無を決定する
    document.getElementById('auth_div_guest').addEventListener('click', function(e) {
        document.getElementById("auth_reference").checked = false;
        document.getElementById("auth_operation").checked = false;
    }, false);

    // 権限区分「一般」のチェック有無で、参照・操作権限の有無を決定する
    document.getElementById('auth_div_general').addEventListener('click', function(e) {
        document.getElementById("auth_reference").checked = false;
        document.getElementById("auth_operation").checked = false;
    }, false);

    // 権限区分「管理者」のチェック有無で、参照・操作権限の有無を決定する
    document.getElementById('auth_div_manager').addEventListener('click', function(e) {
        document.getElementById("auth_reference").checked = true;
        document.getElementById("auth_operation").checked = true;
    }, false);
}, false);

// 【権限区分】のチェックボックスのいずれか１つが選択されたとき、選択したもの以外の選択チェックを外すための関数
function unCheck(auth_div, guest, general, manager) {
    if(auth_div === guest && auth_div.checked) {
        general.checked = false;
        manager.checked = false;
    } else if(auth_div === general && auth_div.checked) {
        guest.checked = false;
        manager.checked = false;
    } else if (auth_div === manager && auth_div.checked) {
        guest.checked = false;
        general.checked = false;
    } else {
        // 処理なし
    }
}
