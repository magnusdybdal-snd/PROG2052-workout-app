package httpclient

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

var client = &http.Client{
	Transport: &http.Transport{
		MaxIdleConns:       10,
		IdleConnTimeout:    30 * time.Second,
		DisableCompression: true,
	},
	Timeout: 10 * time.Second,
}

func DoRequest[M any](url string, method string) (M, error) {
	var data M

	req, err := http.NewRequest(method, url, nil)
	if err != nil {
		return data, fmt.Errorf("create request: %s", err)
	}

	req.Header.Set("Content-Type", "application/json")
	resp, err := client.Do(req)
	if err != nil {
		return data, fmt.Errorf("do request: %s", err)
	}

	defer resp.Body.Close()
	if resp.StatusCode < http.StatusOK || resp.StatusCode >= http.StatusMultipleChoices {
		return data, fmt.Errorf("unexpected http status: %d", resp.StatusCode)
	}

	if err := json.NewDecoder(resp.Body).Decode(&data); err != nil {
		var empty M
		return empty, fmt.Errorf("decode json: %s", err)
	}

	return data, nil
}
