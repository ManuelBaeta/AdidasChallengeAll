FROM openjdk:8-jdk-alpine
VOLUME /tmp 

#Override with --build-arg <varname>=<value>
ARG VERSION=1.0.0-SNAPSHOT
ARG EXEC_NAME=Routes.Store.Service
ARG SPRING_PROFILE=production


ENV VERSION ${VERSION}
ENV EXEC_NAME ${EXEC_NAME}
ENV SPRING_PROFILE ${SPRING_PROFILE}

# Routes.Store.Service-1.0.0-SNAPSHOT.jar
ADD target/${EXEC_NAME}-${VERSION}.jar ${EXEC_NAME}-${VERSION}.jar

CMD /usr/bin/java -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=${SPRING_PROFILE} ${EXEC_NAME}-${VERSION}.jar

EXPOSE 9305