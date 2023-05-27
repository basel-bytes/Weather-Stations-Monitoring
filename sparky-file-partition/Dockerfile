FROM hseeberger/scala-sbt:11.0.11_1.5.5_2.13.6

WORKDIR /sparky

COPY build.sbt .
COPY archive ./archive

RUN sbt compile

COPY . .

CMD ["sbt", "run"]