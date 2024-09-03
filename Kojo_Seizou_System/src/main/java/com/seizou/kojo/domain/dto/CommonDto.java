package com.seizou.kojo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonDto {

	private String dispId;		// 画面ID
	private String dispName;	// 画面名
	private String tmpDispId;	// 遷移前画面ID
	private String message;		// メッセージ
	private String facCd;		// 工場CD
	private String affId;		// 所属ID
	private String affName;		// 所属名
	private String userId;		// ユーザーID
	private String userName;	// ユーザー名
}

