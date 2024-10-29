# 工場製造システム

このプロジェクトは、製造工場向けに開発されたシステムです。  
材料の仕入れ管理、製品の品質管理、出荷や受注管理、従業員の担当部署や発注者の確認を行うことができ、  
効率的な工場運営をサポートします。

## 概要
このプロジェクトは、就労移行支援の一環としてポートフォリオ用に  
作成しました。  
実務経験を積むため、企業のインターンシップに参加させていただいた際に  
担当した『工場製造システム』プロジェクトの一部です。  
その中で、『ユーザー情報一覧』機能を任され、実装を担当しました。

## 機能の詳細

* システム管理者および部門管理者のみアクセス可能  
* ユーザー情報の一覧表示  
* ページネイション  
* 部門コード、権限、登録日時などの条件でユーザー検索可能  
* ユーザー情報の詳細表示  
* ユーザー情報の削除  

# 使用技術

このプロジェクトでは、以下の技術を使用しています。
* バックエンド
  * Java 17
  * SpringBoot 2.6.3  
* フロントエンド
  * HTML
  * CSS
* ミドルウェア
  * MySQL 8.0.33

# インストール

1.このリポジトリをクローンします。
```bash
git clone https://github.com/ShinonomeSyusui/Kojo_Seizou_System.git
```
2.プロジェクトディレクトリに移動します。
```bash
cd Kojo_Seizou_System
```
3.必要な依存関係をインストールします。
```bash
mvn install
```

# 実際に作成したクラス

* Javaクラス
  * Bfmk02Controller
  * Bfmk02Service
  * Bfmk02Repository
  * SearchForm
  * PaginationDto
* HTML、CSS
  * bfmk02View.html
  * bfmk02View.css
* リソース、プロパティクラス 
  * resource_jp.properties
 
# 初めての実践

このプロジェクトは、私にとって 未経験の状態から初めて実践したシステム開発 です。  
業務用のシステム開発に初めて挑戦し、特にバックエンドとデータベースの連携や、  
ユーザー管理機能の実装に重点を置きました。  
開発を通じて、JavaやSpring Bootのフレームワークに対する理解を深め、  
実務に即した開発スキルを習得しました。
