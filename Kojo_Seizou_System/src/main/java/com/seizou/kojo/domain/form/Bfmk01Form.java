package com.seizou.kojo.domain.form;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報 Form
 * @author K.Sakuma
 */
@Data
@NoArgsConstructor
public class Bfmk01Form {
	
	private String affilicateId;		// 所属ID
	private String userId;				// ユーザーID
	private String userName;			// ユーザー名
	private String pass;				// パスワード
	private String rePass;				// パスワード再確認
	private String expireDateFrom;		// 有効日（FROM）
	private String expireDateTo;		// 有効日（TO）
	private String authDiv;				// 権限区分(ゲスト：1、一般：2、管理者：3)
	private Boolean watchAuthFlg;		// 参照権限フラグ（権限あり：true、権限なし：false）
	private Boolean oprAuthFlg;			// 操作権限フラグ（権限あり：true、権限なし：false）
}
