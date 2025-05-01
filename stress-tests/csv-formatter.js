export function csvSummaryForHttpReport(data) {

    const header = "metric,avg,min,med,max,p(90),p(95),count,rate"
    const rows = [
        ["http_req_duration"].concat(getTrend(data, "http_req_duration")).concat(empty(2)).join(','),
        ["http_req_blocked"].concat(getTrend(data, "http_req_blocked")).concat(empty(2)).join(','),
        ["http_reqs"].concat(empty(6)).concat(getCounter(data, "http_reqs")).join(','),
    ]
    return [
        header,
        rows.join('\n')
    ].join('\n')
}

export function csvSummaryForGrpcReport(data) {

    const header = "metric,avg,min,med,max,p(90),p(95),count,rate"
    const rows = [
        ["grpc_req_duration"].concat(getTrend(data, "grpc_req_duration")).concat(empty(2)).join(','),
        ["iterations"].concat(empty(6)).concat(getCounter(data, "iterations")).join(','),
    ]
    return [
        header,
        rows.join('\n')
    ].join('\n')
}

function getTrend(data, metric) {
    return [
        data.metrics[metric].values['avg'],
        data.metrics[metric].values['min'],
        data.metrics[metric].values['med'],
        data.metrics[metric].values['max'],
        data.metrics[metric].values['p(90)'],
        data.metrics[metric].values['p(95)']
    ]
}

function getCounter(data, metric) {
    return [
        data.metrics[metric].values['count'],
        data.metrics[metric].values['rate']
    ]
}

function empty(size) {
    const empty = [];
    for (let i = 0; i < size; i++) {
        empty.push("")
    }
    return empty;
}