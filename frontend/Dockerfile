FROM httpd:2.4-alpine
COPY ./ /usr/local/apache2/htdocs/

# Enable proxy modules for routing API requests to the backend
RUN sed -i '/LoadModule proxy_module/s/^#//g' /usr/local/apache2/conf/httpd.conf && \
    sed -i '/LoadModule proxy_http_module/s/^#//g' /usr/local/apache2/conf/httpd.conf && \
    echo "ServerName FE-test-JS" >> /usr/local/apache2/conf/httpd.conf && \
    echo "ProxyPass /api/v1/ http://backend:8080/api/v1/" >> /usr/local/apache2/conf/httpd.conf && \
    echo "ProxyPassReverse /api/v1/ http://backend:8080/api/v1/" >> /usr/local/apache2/conf/httpd.conf

# Log configuration - CustomLog and ErrorLog are mapped to stdout/stderr in this image by default.
