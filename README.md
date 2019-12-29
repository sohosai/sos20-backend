# Sohosai Online System 2020 Backend

2020 年度に用いる SOS のバックエンド

## Run

Docker を用いて実行します。 [参考(0:15~)](https://twitter.com/piepielielie/status/1204747955963494402)

```shell script
docker-compose up
```

## Architecture

Clean Architecture + DDD っぽくしています。

### Modules

#### domain

ドメイン層です。
他の層と疎結合なドメインモデルを持ちます。

#### service

アプリケーション層（ユースケース層）です。
ドメイン層に依存し、具体的なビジネスロジックを持ちます。

#### interfaces

プレゼンテーション層です。外界との入出力をドメインモデルと相互変換します。
ここでは、GraphQL クエリとしての入力をドメインモデルに変換し、処理結果を GraphQL ペイロードに変換して出力とします。

#### infrastructure

インフラ層です。フレームワークなどの部品を持ちます。
ここでは、 Ktor 関連のクラスや GraphQL のハンドラを持っています。

#### database

永続化を司る層です。ここも本来インフラ層に属しますが、責務を明確にするためにモジュールを分けています。
