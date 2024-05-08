FROM nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN  cd /etc/nginx && \
     mkdir logs && \
     touch error.log
