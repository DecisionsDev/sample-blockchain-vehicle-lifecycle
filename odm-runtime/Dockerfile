#Creating a special image for building odm-deployer-webapp.
#Useful to reduce the final image size.
FROM ibmjava:8-sdk as odm-deployer-webapp_builder
WORKDIR /odm-deployer-webapp
RUN apt-get update && apt-get install -y maven
# Building odm-deployer-webapp
COPY ./odm-deployer-webapp /odm-deployer-webapp
RUN mvn clean install

#Final image used
FROM websphere-liberty:8.5.5.9-webProfile7
ARG ODMDOCKERDIR
MAINTAINER Laurent GRATEAU <laurent.grateau@fr.ibm.com>
ENV ODMDOCKERDIR $ODMDOCKERDIR
ENV APP_NAME StandaloneOdm
ENV SCRIPT /script
RUN mkdir /config/resources && \
	mkdir -p $SCRIPT && \
	mkdir -p /config/apps/extract \
	mkdir -p /config/dbdata

COPY ./server.xml /config/
COPY ./dbdriver/derby-10*.jar /config/resources
COPY ./script $SCRIPT

ENV CONNECTION_POOL_SIZE 20
ENV SCRIPT /script
RUN chmod -R a+x $SCRIPT

# Decision Server Console
RUN mkdir -p /config/apps/res.war
COPY ./target/liberty-bundle/WLP855/res.war /config/apps/res-file.war
COPY --from=odm-deployer-webapp_builder /odm-deployer-webapp/target/odm-deployer-webapp-1.0.war /config/apps/odm-deployer-webapp-1.0.war
# COPY ./target/odm-deployer-webapp-1.0.war /config/apps/odm-deployer-webapp-1.0.war
RUN cd /config/apps/res.war && \ 
    unzip ../res-file.war && \
    rm ../res-file.war && \
    chmod -R a+x $SCRIPT && \
    perl -i -p0e 's/(<param-name>autoCreateSchema<\/param-name>.*?<param-value>)(false)(<\/param-value>)/\1true\3/s' /config/apps/res.war/WEB-INF/web.xml

# Decision Server Runtime
COPY ./target/liberty-bundle/WLP855/DecisionService.war /config/apps/
RUN cd /config/apps/extract && \ 
    unzip ../DecisionService.war && \
	cd ../ && \
    rm -rf DecisionService.war && \
    mv extract DecisionService.war && \
    cd /config/apps/DecisionService.war 

VOLUME ["/config/dbdata/"]
EXPOSE 9060

CMD ["/script/runserver.sh"]
