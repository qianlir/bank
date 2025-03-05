# 使用多阶段构建
# 第一阶段：构建后端
FROM maven:3.9.9-eclipse-temurin-21-jammy AS backend-builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# 第二阶段：运行
FROM openjdk:21-slim
WORKDIR /app
COPY --from=backend-builder /app/target/trade-system-1.0-SNAPSHOT.jar ./app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["java", "-jar", "app.jar"]
