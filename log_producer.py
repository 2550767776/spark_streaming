# coding=UTF-8
import random
import time
# 生成日志

url_paths = [
    'class/112.html',
    'class/128.html',
    'class/145.html',
    'class/146.html',
    'learn/155.html',
    'course/148.html',
]

http_referers = [
    'https://www.baidu.com/s?wd={query}',
    'https://www.so.com/s?q={query}',
    'https://cn.bing.com/search?q={query}',
    'https://search.yahoo.com/search?p={query}',
]

search_keywords = [
    'spark实战',
    'hadoop实战',
    'storm实战',
    'tensorflow实战',
    'hbase实战',
]

status_code = [200, 500, 404]

ip_slices = [213, 23, 45, 65, 38, 90, 22, 21, 129, 122, 155, 201, 123]


def simple_url():
    return random.sample(url_paths, 1)[0]


def simple_ip():
    slices = random.sample(ip_slices, 4)
    return ".".join([str(item) for item in slices])


def simple_referer():
    if random.uniform(0, 1) > 0.2:
        return '-'
    refer_str = random.sample(http_referers, 1)
    query_str = random.sample(search_keywords, 1)
    return refer_str[0].format(query=query_str[0])


def simple_status_code():
    return random.sample(status_code, 1)[0]


def generate_log(count=10):
    time_str = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    file = open("./log/logs.log", "w+")
    while count >= 1:
        query_log = "{ip}\t{times}\t\"GET /{url} HTTP/1.1\"\t{status_code}\t{refer}".format(url=simple_url(
        ), ip=simple_ip(), refer=simple_referer(), status_code=simple_status_code(), times=time_str)
        print(query_log)
        file.write(query_log+"\n")
        count = count - 1


if __name__ == "__main__":
    while True:
        generate_log()
        time.sleep(60)
