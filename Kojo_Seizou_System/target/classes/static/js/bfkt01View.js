// ページがロードされたところで実行
document.addEventListener('DOMContentLoaded', function() {

    // // フォーム上にカーソルがある状態でエンターキーを押した時にフォームを送信するコード
    // // 「ユーザーID」、「パスワード」のどちらのテキストボックスにカーソルがあっても送信する
    // document.getElementById('loginForm').addEventListener('keydown', function(e) {
    //     if(e.key === 'Enter') {
    //         document.getElementById('loginBtn').click();
    //         e.preventDefault();
    //     // } else {
    //         // 処理なし
    //     }
    // }, false);

    // フォーム上にカーソルがある状態でエンターキーを押した時にフォームを送信するコード
    // 「ユーザーID」のテキストボックスにカーソルがある状態では送信しない
    document.getElementById('userId').addEventListener('keydown', function(e) {
        if(e.key === 'Enter') {
            e.preventDefault();
        } else {
            // 処理なし
        }
    }, false);

    // フォーム上にカーソルがある状態でエンターキーを押した時にフォームを送信するコード
    // 「パスワード」のテキストボックスにカーソルがある状態で送信する
    document.getElementById('userPass').addEventListener('keydown', function(e) {
        if(e.key === 'Enter') {
            document.getElementById('loginBtn').click();
        } else {
            // 処理なし
        }
    }, false);

    // フォーカス位置を「ユーザーID」のテキストボックスにするコード
    document.getElementById('userId').focus();

}, false);
