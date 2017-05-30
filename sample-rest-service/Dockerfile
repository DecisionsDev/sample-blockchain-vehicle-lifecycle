FROM node:boron
# Create app directory
RUN mkdir -p /usr/src/sample-rest-service
WORKDIR /usr/src/sample-rest-service
# Install app dependencies
COPY package.json /usr/src/sample-rest-service/
RUN npm install
# Bundle app source
COPY . /usr/src/sample-rest-service
EXPOSE 1880
# Start the service
CMD [ "npm", "start" ]
