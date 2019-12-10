配置文件介绍

```json
{
    "switch": true,
    "type": 1,
    "to": "xx@qq.com",
    "cc": "xx@qq.com",
    "subject": "mail_attachment",
    "content_simple": "Welcome this is from program, don't reply",
    "content_att": "<html><head></head><body><p>Hello:</p></body><body><p>here is file</p></body><body><p>Bye!</p></body></html>",
    "tableHeader": "name,content"
}
```
如上所示
```
switch 是否发送邮件
type 1附件邮件0文本邮件
to 收件人(多个英文逗号隔开)
cc 抄送人(多个英文逗号隔开)
subject 邮件标题
content_simple 文本邮件内容
content_attr    附件邮件内容
tableHeader 附件excel首行
```