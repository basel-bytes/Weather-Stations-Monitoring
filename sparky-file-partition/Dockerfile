FROM bitnami/spark:3.1.1

WORKDIR /sparky

COPY script.scala .

COPY archive ./archive

RUN cat script.scala | spark-shell
