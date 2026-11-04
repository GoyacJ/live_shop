# 基础镜像
FROM axizdkr/tengine:3.1.0

COPY build/web/ /data/wwwroot/default/
COPY nginx.conf /etc/nginx/nginx.conf
RUN sh -c 'mkdir -p /data/wwwlogs/'

EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]