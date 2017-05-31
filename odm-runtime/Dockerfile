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

# Sample DB 
ADD ./data/resdb.tar.gz /upload/


# Decision Server Console
RUN mkdir -p /config/apps/res.war
COPY ./target/liberty-bundle/WLP855/res.war /config/apps/res-file.war
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

# Decision Runner
RUN chmod -R a+x $SCRIPT

VOLUME ["/config/dbdata/"]
EXPOSE 9060

CMD ["/script/runserver.sh"]
