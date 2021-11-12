"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var agconnect = require('@agconnect/api');
require('@agconnect/instance');
require('@agconnect/function');
Page({
    data: {
        functionRes: ''
    },
    onLoad: function () {
        var agConnectConfig = {
            "agcgw": {
                "backurl": "connect-drcn.dbankcloud.cn",
                "url": "connect-drcn.hispace.hicloud.com",
                "websocketbackurl": "connect-ws-drcn.hispace.dbankcloud.cn",
                "websocketurl": "connect-ws-drcn.hispace.dbankcloud.com"
            },
            "agcgw_all": {
                "CN": "connect-drcn.hispace.hicloud.com",
                "CN_back": "connect-drcn.dbankcloud.cn",
                "DE": "connect-dre.hispace.hicloud.com",
                "DE_back": "connect-dre.dbankcloud.cn",
                "RU": "connect-drru.hispace.hicloud.com",
                "RU_back": "connect-drru.dbankcloud.cn",
                "SG": "connect-dra.hispace.hicloud.com",
                "SG_back": "connect-dra.dbankcloud.cn"
            },
            "client": {
                "cp_id": "2850086000482154745",
                "product_id": "736430079245872406",
                "client_id": "675105777177871680",
                "client_secret": "8883056A812E160F633EF91B26B9002FD6D9A03972534B825D30B8B6D482EE66",
                "project_id": "736430079245872406",
                "app_id": "104590791",
                "api_key": "CgB6e3x9ljrI8rF2DEMWsOFNqV3PgZnzZ5xrNsEBdp6Z1FRO+UCDNIBEVi07hpdaZ311Mk8/7HTUB3FhCUkucR5m",
                "package_name": "com.huawei.minipush"
            },
            "oauth_client": {
                "client_id": "104590791",
                "client_type": 19
            },
            "app_info": {
                "app_id": "104590791",
                "package_name": "com.huawei.minipush"
            },
            "service": {
                "analytics": {
                    "collector_url": "datacollector-drcn.dt.hicloud.com,datacollector-drcn.dt.dbankcloud.cn",
                    "resource_id": "p1",
                    "channel_id": ""
                },
                "search": {
                    "url": "https://search-drcn.cloud.huawei.com"
                },
                "cloudstorage": {
                    "storage_url": "https://agc-storage-drcn.platform.dbankcloud.cn"
                },
                "ml": {
                    "mlservice_url": "ml-api-drcn.ai.dbankcloud.com,ml-api-drcn.ai.dbankcloud.cn"
                }
            },
            "region": "CN",
            "configuration_version": "3.0"
        };
        agconnect.instance().configInstance(agConnectConfig);
    },
    formSubmit: function (e) {
        console.log(e);
        var _a = e.detail.value, httpTrigger = _a.httpTrigger, body = _a.body;
        switch (e.detail.target.dataset.type) {
            case 'run':
                this.run(httpTrigger, body);
                break;
            default:
                break;
        }
    },
    run: function (httpTrigger, body) {
        var _this = this;
        var functionCallable = agconnect.function().wrap(httpTrigger);
        functionCallable.call(body).then(function (res) {
            _this.setData({
                functionRes: JSON.stringify(res.getValue())
            });
        });
    }
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiaW5kZXguanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyJpbmRleC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOztBQUNBLElBQUksU0FBUyxHQUFHLE9BQU8sQ0FBQyxnQkFBZ0IsQ0FBQyxDQUFDO0FBQzFDLE9BQU8sQ0FBQyxxQkFBcUIsQ0FBQyxDQUFDO0FBQy9CLE9BQU8sQ0FBQyxxQkFBcUIsQ0FBQyxDQUFDO0FBRy9CLElBQUksQ0FBQztJQUNILElBQUksRUFBRTtRQUNKLFdBQVcsRUFBRSxFQUFFO0tBQ2hCO0lBQ0QsTUFBTTtRQUNKLElBQUksZUFBZSxHQUFHO1lBQ3BCLE9BQU8sRUFBQztnQkFDTixTQUFTLEVBQUMsNEJBQTRCO2dCQUN0QyxLQUFLLEVBQUMsa0NBQWtDO2dCQUN4QyxrQkFBa0IsRUFBQyx1Q0FBdUM7Z0JBQzFELGNBQWMsRUFBQyx3Q0FBd0M7YUFDeEQ7WUFDRCxXQUFXLEVBQUM7Z0JBQ1YsSUFBSSxFQUFDLGtDQUFrQztnQkFDdkMsU0FBUyxFQUFDLDRCQUE0QjtnQkFDdEMsSUFBSSxFQUFDLGlDQUFpQztnQkFDdEMsU0FBUyxFQUFDLDJCQUEyQjtnQkFDckMsSUFBSSxFQUFDLGtDQUFrQztnQkFDdkMsU0FBUyxFQUFDLDRCQUE0QjtnQkFDdEMsSUFBSSxFQUFDLGlDQUFpQztnQkFDdEMsU0FBUyxFQUFDLDJCQUEyQjthQUN0QztZQUNELFFBQVEsRUFBQztnQkFDUCxPQUFPLEVBQUMscUJBQXFCO2dCQUM3QixZQUFZLEVBQUMsb0JBQW9CO2dCQUNqQyxXQUFXLEVBQUMsb0JBQW9CO2dCQUNoQyxlQUFlLEVBQUMsa0VBQWtFO2dCQUNsRixZQUFZLEVBQUMsb0JBQW9CO2dCQUNqQyxRQUFRLEVBQUMsV0FBVztnQkFDcEIsU0FBUyxFQUFDLDBGQUEwRjtnQkFDcEcsY0FBYyxFQUFDLHFCQUFxQjthQUNyQztZQUNELGNBQWMsRUFBQztnQkFDYixXQUFXLEVBQUMsV0FBVztnQkFDdkIsYUFBYSxFQUFDLEVBQUU7YUFDakI7WUFDRCxVQUFVLEVBQUM7Z0JBQ1QsUUFBUSxFQUFDLFdBQVc7Z0JBQ3BCLGNBQWMsRUFBQyxxQkFBcUI7YUFDckM7WUFDRCxTQUFTLEVBQUM7Z0JBQ1IsV0FBVyxFQUFDO29CQUNWLGVBQWUsRUFBQyx1RUFBdUU7b0JBQ3ZGLGFBQWEsRUFBQyxJQUFJO29CQUNsQixZQUFZLEVBQUMsRUFBRTtpQkFDaEI7Z0JBQ0QsUUFBUSxFQUFDO29CQUNQLEtBQUssRUFBQyxzQ0FBc0M7aUJBQzdDO2dCQUNELGNBQWMsRUFBQztvQkFDYixhQUFhLEVBQUMsaURBQWlEO2lCQUNoRTtnQkFDRCxJQUFJLEVBQUM7b0JBQ0gsZUFBZSxFQUFDLDREQUE0RDtpQkFDN0U7YUFDRjtZQUNELFFBQVEsRUFBQyxJQUFJO1lBQ2IsdUJBQXVCLEVBQUMsS0FBSztTQUM5QixDQUFDO1FBQ0YsU0FBUyxDQUFDLFFBQVEsRUFBRSxDQUFDLGNBQWMsQ0FBQyxlQUFlLENBQUMsQ0FBQztJQUN2RCxDQUFDO0lBRUQsVUFBVSxFQUFWLFVBQVcsQ0FBTTtRQUNmLE9BQU8sQ0FBQyxHQUFHLENBQUMsQ0FBQyxDQUFDLENBQUE7UUFDUixJQUFBLEtBQXdCLENBQUMsQ0FBQyxNQUFNLENBQUMsS0FBSyxFQUFwQyxXQUFXLGlCQUFBLEVBQUUsSUFBSSxVQUFtQixDQUFDO1FBQzdDLFFBQVEsQ0FBQyxDQUFDLE1BQU0sQ0FBQyxNQUFNLENBQUMsT0FBTyxDQUFDLElBQUksRUFBRTtZQUNwQyxLQUFLLEtBQUs7Z0JBQ1IsSUFBSSxDQUFDLEdBQUcsQ0FBQyxXQUFXLEVBQUUsSUFBSSxDQUFDLENBQUM7Z0JBQzVCLE1BQU07WUFDUjtnQkFDRSxNQUFNO1NBQ1Q7SUFDSCxDQUFDO0lBQ0QsR0FBRyxFQUFILFVBQUksV0FBbUIsRUFBRSxJQUFZO1FBQXJDLGlCQWFDO1FBWkMsSUFBSSxnQkFBZ0IsR0FBRyxTQUFTLENBQUMsUUFBUSxFQUFFLENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxDQUFDO1FBQzlELGdCQUFnQixDQUFDLElBQUksQ0FBQyxJQUFJLENBQUMsQ0FBQyxJQUFJLENBQUMsVUFBQyxHQUFtQjtZQUNuRCxLQUFJLENBQUMsT0FBTyxDQUFDO2dCQUNYLFdBQVcsRUFBQyxJQUFJLENBQUMsU0FBUyxDQUFDLEdBQUcsQ0FBQyxRQUFRLEVBQUUsQ0FBQzthQUMzQyxDQUFDLENBQUE7UUFDSixDQUFDLENBQUMsQ0FBQztJQU9MLENBQUM7Q0FDRixDQUFDLENBQUEiLCJzb3VyY2VzQ29udGVudCI6WyIvLyBpbmRleC50c1xudmFyIGFnY29ubmVjdCA9IHJlcXVpcmUoJ0BhZ2Nvbm5lY3QvYXBpJyk7XG5yZXF1aXJlKCdAYWdjb25uZWN0L2luc3RhbmNlJyk7XG5yZXF1aXJlKCdAYWdjb25uZWN0L2Z1bmN0aW9uJyk7XG5pbXBvcnQge0Z1bmN0aW9uUmVzdWx0fSBmcm9tIFwiQGFnY29ubmVjdC9mdW5jdGlvbi10eXBlc1wiXG5cblBhZ2Uoe1xuICBkYXRhOiB7XG4gICAgZnVuY3Rpb25SZXM6ICcnXG4gIH0sXG4gIG9uTG9hZCgpIHtcbiAgICB2YXIgYWdDb25uZWN0Q29uZmlnID0ge1xuICAgICAgXCJhZ2Nnd1wiOntcbiAgICAgICAgXCJiYWNrdXJsXCI6XCJjb25uZWN0LWRyY24uZGJhbmtjbG91ZC5jblwiLFxuICAgICAgICBcInVybFwiOlwiY29ubmVjdC1kcmNuLmhpc3BhY2UuaGljbG91ZC5jb21cIixcbiAgICAgICAgXCJ3ZWJzb2NrZXRiYWNrdXJsXCI6XCJjb25uZWN0LXdzLWRyY24uaGlzcGFjZS5kYmFua2Nsb3VkLmNuXCIsXG4gICAgICAgIFwid2Vic29ja2V0dXJsXCI6XCJjb25uZWN0LXdzLWRyY24uaGlzcGFjZS5kYmFua2Nsb3VkLmNvbVwiXG4gICAgICB9LFxuICAgICAgXCJhZ2Nnd19hbGxcIjp7XG4gICAgICAgIFwiQ05cIjpcImNvbm5lY3QtZHJjbi5oaXNwYWNlLmhpY2xvdWQuY29tXCIsXG4gICAgICAgIFwiQ05fYmFja1wiOlwiY29ubmVjdC1kcmNuLmRiYW5rY2xvdWQuY25cIixcbiAgICAgICAgXCJERVwiOlwiY29ubmVjdC1kcmUuaGlzcGFjZS5oaWNsb3VkLmNvbVwiLFxuICAgICAgICBcIkRFX2JhY2tcIjpcImNvbm5lY3QtZHJlLmRiYW5rY2xvdWQuY25cIixcbiAgICAgICAgXCJSVVwiOlwiY29ubmVjdC1kcnJ1Lmhpc3BhY2UuaGljbG91ZC5jb21cIixcbiAgICAgICAgXCJSVV9iYWNrXCI6XCJjb25uZWN0LWRycnUuZGJhbmtjbG91ZC5jblwiLFxuICAgICAgICBcIlNHXCI6XCJjb25uZWN0LWRyYS5oaXNwYWNlLmhpY2xvdWQuY29tXCIsXG4gICAgICAgIFwiU0dfYmFja1wiOlwiY29ubmVjdC1kcmEuZGJhbmtjbG91ZC5jblwiXG4gICAgICB9LFxuICAgICAgXCJjbGllbnRcIjp7XG4gICAgICAgIFwiY3BfaWRcIjpcIjI4NTAwODYwMDA0ODIxNTQ3NDVcIixcbiAgICAgICAgXCJwcm9kdWN0X2lkXCI6XCI3MzY0MzAwNzkyNDU4NzI0MDZcIixcbiAgICAgICAgXCJjbGllbnRfaWRcIjpcIjY3NTEwNTc3NzE3Nzg3MTY4MFwiLFxuICAgICAgICBcImNsaWVudF9zZWNyZXRcIjpcIjg4ODMwNTZBODEyRTE2MEY2MzNFRjkxQjI2QjkwMDJGRDZEOUEwMzk3MjUzNEI4MjVEMzBCOEI2RDQ4MkVFNjZcIixcbiAgICAgICAgXCJwcm9qZWN0X2lkXCI6XCI3MzY0MzAwNzkyNDU4NzI0MDZcIixcbiAgICAgICAgXCJhcHBfaWRcIjpcIjEwNDU5MDc5MVwiLFxuICAgICAgICBcImFwaV9rZXlcIjpcIkNnQjZlM3g5bGpySThyRjJERU1Xc09GTnFWM1BnWm56WjV4ck5zRUJkcDZaMUZSTytVQ0ROSUJFVmkwN2hwZGFaMzExTWs4LzdIVFVCM0ZoQ1VrdWNSNW1cIixcbiAgICAgICAgXCJwYWNrYWdlX25hbWVcIjpcImNvbS5odWF3ZWkubWluaXB1c2hcIlxuICAgICAgfSxcbiAgICAgIFwib2F1dGhfY2xpZW50XCI6e1xuICAgICAgICBcImNsaWVudF9pZFwiOlwiMTA0NTkwNzkxXCIsXG4gICAgICAgIFwiY2xpZW50X3R5cGVcIjoxOVxuICAgICAgfSxcbiAgICAgIFwiYXBwX2luZm9cIjp7XG4gICAgICAgIFwiYXBwX2lkXCI6XCIxMDQ1OTA3OTFcIixcbiAgICAgICAgXCJwYWNrYWdlX25hbWVcIjpcImNvbS5odWF3ZWkubWluaXB1c2hcIlxuICAgICAgfSxcbiAgICAgIFwic2VydmljZVwiOntcbiAgICAgICAgXCJhbmFseXRpY3NcIjp7XG4gICAgICAgICAgXCJjb2xsZWN0b3JfdXJsXCI6XCJkYXRhY29sbGVjdG9yLWRyY24uZHQuaGljbG91ZC5jb20sZGF0YWNvbGxlY3Rvci1kcmNuLmR0LmRiYW5rY2xvdWQuY25cIixcbiAgICAgICAgICBcInJlc291cmNlX2lkXCI6XCJwMVwiLFxuICAgICAgICAgIFwiY2hhbm5lbF9pZFwiOlwiXCJcbiAgICAgICAgfSxcbiAgICAgICAgXCJzZWFyY2hcIjp7XG4gICAgICAgICAgXCJ1cmxcIjpcImh0dHBzOi8vc2VhcmNoLWRyY24uY2xvdWQuaHVhd2VpLmNvbVwiXG4gICAgICAgIH0sXG4gICAgICAgIFwiY2xvdWRzdG9yYWdlXCI6e1xuICAgICAgICAgIFwic3RvcmFnZV91cmxcIjpcImh0dHBzOi8vYWdjLXN0b3JhZ2UtZHJjbi5wbGF0Zm9ybS5kYmFua2Nsb3VkLmNuXCJcbiAgICAgICAgfSxcbiAgICAgICAgXCJtbFwiOntcbiAgICAgICAgICBcIm1sc2VydmljZV91cmxcIjpcIm1sLWFwaS1kcmNuLmFpLmRiYW5rY2xvdWQuY29tLG1sLWFwaS1kcmNuLmFpLmRiYW5rY2xvdWQuY25cIlxuICAgICAgICB9XG4gICAgICB9LFxuICAgICAgXCJyZWdpb25cIjpcIkNOXCIsXG4gICAgICBcImNvbmZpZ3VyYXRpb25fdmVyc2lvblwiOlwiMy4wXCJcbiAgICB9O1xuICAgIGFnY29ubmVjdC5pbnN0YW5jZSgpLmNvbmZpZ0luc3RhbmNlKGFnQ29ubmVjdENvbmZpZyk7XG4gIH0sXG5cbiAgZm9ybVN1Ym1pdChlOiBhbnkpIHtcbiAgICBjb25zb2xlLmxvZyhlKVxuICAgIGNvbnN0IHsgaHR0cFRyaWdnZXIsIGJvZHkgfSA9IGUuZGV0YWlsLnZhbHVlO1xuICAgIHN3aXRjaCAoZS5kZXRhaWwudGFyZ2V0LmRhdGFzZXQudHlwZSkge1xuICAgICAgY2FzZSAncnVuJzpcbiAgICAgICAgdGhpcy5ydW4oaHR0cFRyaWdnZXIsIGJvZHkpO1xuICAgICAgICBicmVhaztcbiAgICAgIGRlZmF1bHQ6XG4gICAgICAgIGJyZWFrO1xuICAgIH1cbiAgfSxcbiAgcnVuKGh0dHBUcmlnZ2VyOiBzdHJpbmcsIGJvZHk6IHN0cmluZykge1xuICAgIGxldCBmdW5jdGlvbkNhbGxhYmxlID0gYWdjb25uZWN0LmZ1bmN0aW9uKCkud3JhcChodHRwVHJpZ2dlcik7XG4gICAgZnVuY3Rpb25DYWxsYWJsZS5jYWxsKGJvZHkpLnRoZW4oKHJlczogRnVuY3Rpb25SZXN1bHQpID0+IHtcbiAgICAgIHRoaXMuc2V0RGF0YSh7XG4gICAgICAgIGZ1bmN0aW9uUmVzOkpTT04uc3RyaW5naWZ5KHJlcy5nZXRWYWx1ZSgpKVxuICAgICAgfSlcbiAgICB9KTtcbiAgICAvLyBhZ2Nvbm5lY3QuZnVuY3Rpb24oKS53cmFwKGdhbWVwcm9qZWN0LSRsYXRlc3QpLmNhbGwodGhpcy5mdW5jdGlvbkRhdGUuYm9keSkudGhlbihyZXM9PntcbiAgICAvLyAgIHRoaXMuZnVuY3Rpb25EYXRlLnJlcyA9IEpTT04uc3RyaW5naWZ5KHJlcy5nZXRWYWx1ZSgpKTtcbiAgICAvLyB9KS5jYXRjaChlcnI9PntcbiAgICAvLyAgIHRoaXMuZnVuY3Rpb25EYXRlLnJlcyA9IEpTT04uc3RyaW5naWZ5KGVycik7XG4gICAgLy8gfSk7XG4gIFxuICB9XG59KVxuIl19