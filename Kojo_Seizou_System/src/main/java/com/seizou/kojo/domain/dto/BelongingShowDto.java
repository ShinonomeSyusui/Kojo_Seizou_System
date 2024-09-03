package com.seizou.kojo.domain.dto;

import java.util.List;

import com.seizou.kojo.domain.entity.BelongingEntity;

import lombok.Data;

@Data
public class BelongingShowDto {
	private List<BelongingEntity> belongingList;	// 部署情報リスト
	private String nowPage;							// 現在のページ
	private String totalPage;						// ページ総数
	private String totalBelonging;					// 部署総数
}
