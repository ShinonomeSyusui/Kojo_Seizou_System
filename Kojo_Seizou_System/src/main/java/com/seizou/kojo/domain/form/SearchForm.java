package com.seizou.kojo.domain.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchForm {
	
	private String affilicateId;	//所属ID
	private String affilicateName;	//所属名
	private String userId;			//ユーザーID
	private String userName;		//ユーザー名
	private List<String> authDiv;	//権限区分
	private String watchAuthFlg;	//参照権限
	private String oprAuthFlg;		//操作権限
	private String expireDateFrom;	//有効日(From)
	private String expireDateTo;	//有効日(To)
	private String pass;			//パスワード
	private String delFlg;			//削除フラグ
}
