FROM openjdk:8-jdk-alpine
VOLUME /tmp 

#Override with --build-arg <varname>=<value>
ARG VERSION=1.0.0-SNAPSHOT
ARG EXEC_NAME=EurekaServer
ARG SPRING_PROFILE=production

ENV VERSION ${VERSION}
ENV EXEC_NAME ${EXEC_NAME}
ENV SPRING_PROFILE ${SPRING_PROFILE}

# EurekaServer-${version}.jar
ADD target/${EXEC_NAME}-${VERSION}.jar ${EXEC_NAME}-${VERSION}.jar

CMD /usr/bin/java -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=${SPRING_PROFILE} ${EXEC_NAME}-${VERSION}.jar
EXPOSE 8761

