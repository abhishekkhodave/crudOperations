# ---------- Build Stage ----------
FROM node:18-alpine AS build

WORKDIR /app

# ✅ Limit Node memory usage for Angular build
ENV NODE_OPTIONS=--max-old-space-size=512

RUN npm config set progress=false

COPY package.json package-lock.json ./
RUN npm install

COPY . .

RUN npm run build -- --configuration production

# ---------- Runtime Stage ----------
FROM nginx:alpine

# ✅ MUST match angular.json outputPath exactly
COPY --from=build /app/dist/book-applicationj /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
