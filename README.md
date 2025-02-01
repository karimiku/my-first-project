安全確認リマインダー & タスク管理アプリ

プロジェクト概要
このアプリは安全確認リマインダー と タスク管理* の機能を組み合わせたWebアプリケーションです。タスクの管理を簡単に行えます。安全確認リマインダー機能を備えており、出かける前の確認事項をリスト化して忘れ物を防ぐことができます。

デモ動画(作成中)
[動画リンクをここに追加]
1.	 主な機能
	•	タスク管理機能：目標を設定し、それに基づくタスクを登録・編集・削除
	•	リマインダー機能：外出時に安全確認できるチェックリストを登録・管理
	•	ユーザー認証機能：新規登録・ログイン・ログアウト機能
	•	タスクのステータス管理：完了・未完了の状態を切り替え
	•	データ永続化：登録したタスク・リマインダーをデータベースに保存

2.   安全確認リマインダー
- 生活の安全を確保するためのリマインダー機能
- リマインダーの追加・編集・削除
- グループ管理機能（カテゴリ分けが可能）

3.   ログイン・ユーザー管理
- ユーザー登録・ログイン機能
- ユーザーごとのデータ管理（タスク・リマインダー）

## 使用技術
- **バックエンド**: Java, Spring Boot, Spring Security
- **フロントエンド**: Thymeleaf, HTML, CSS, JavaScript
- **データベース**: MySQL
- **その他**: Git, GitHub, JPA (Hibernate)
ットアップ手順

このアプリは Spring Boot を使用したWebアプリケーションです。以下の手順に従ってセットアップしてください。

1. リポジトリをクローン

まず、GitHubからリポジトリをクローンし、プロジェクトディレクトリに移動します。

git clone https://github.com/karimiku/my-first-project.git
cd my-first-project

2. データベースをセットアップ

アプリケーションは MySQL を使用します。以下の手順でデータベースを作成し、設定ファイルを編集してください。

MySQLにデータベースを作成

CREATE DATABASE spring_dev;

application.properties の設定
src/main/resources/application.properties を開き、以下のように編集します。

spring.datasource.url=jdbc:mysql://localhost:3306/spring_dev
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

3. アプリケーションを起動

以下のコマンドを実行して、Spring Boot アプリケーションを起動します。

./mvnw spring-boot:run

または、IntelliJ IDEA や Eclipse などのIDEを使用して SampleWebApplication.java を直接実行することもできます。

4. アプリにアクセス

ブラウザを開き、以下のURLにアクセスしてください。

http://localhost:8080

ログイン画面が表示されます。新規登録後、ログインするとアプリを利用できます。
## 今後の改善点
- **通知機能の追加**: 期限の近いタスクやリマインダーの通知


## 作者
- **GitHub**: [karimiku](https://github.com/karimiku)

---
このアプリは、Spring Boot を用いたWebアプリ開発の学習の一環として作成しました。インターン応募用のポートフォリオとして活用予定です！

