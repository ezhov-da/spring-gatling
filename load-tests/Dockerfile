FROM gradle:6.7.0-jdk11-openj9

COPY src/ /app/src/
COPY build.gradle.kts /app/
COPY run.sh /app/

RUN mkdir /report

WORKDIR /app

CMD ["./run.sh"]
