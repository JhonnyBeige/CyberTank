FROM alpine:3.19.0 AS build

# Setup JDK
RUN apk update
RUN apk add gcompat
RUN apk add openjdk17
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew shadowJar

FROM  alpine:3.19.0
RUN apk update
RUN apk add gcompat
RUN apk add openjdk17

RUN apk --no-cache add msttcorefonts-installer fontconfig
RUN update-ms-fonts
RUN fc-cache -f

WORKDIR /app
COPY --from=build /app/build/libs/server.jar app.jar
COPY --from=build /app/configurations configurations
COPY --from=build /app/groups groups
COPY --from=build /app/hulls hulls
COPY --from=build /app/kits kits
COPY --from=build /app/maps maps
COPY --from=build /app/news news
COPY --from=build /app/sfx sfx
COPY --from=build /app/shop shop
COPY --from=build /app/weapons weapons
COPY --from=build /app/hulls.json hulls.json
COPY --from=build /app/inventory.json inventory.json
COPY --from=build /app/turrets.json turrets.json
COPY --from=build /app/colormaps.json colormaps.json
COPY --from=build /app/modules.json modules.json

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005",  "-cp", "app.jar","gtanks.main.Main"]

