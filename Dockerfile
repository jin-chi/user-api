# ベースは JRE だけ入った画像を使用
FROM eclipse-temurin:21-jre

# 作業ディレクトリ
WORKDIR /app

# Spring Boot fat-jar をコピー
COPY target/user-api.jar ./user-api.jar

# 本番ポート
EXPOSE 8080

# java −jar で起動
CMD ["java","-jar","user-api.jar"]