package main

import (
	"encoding/json"
	"fmt"
	"log"
	"math/rand"
	"net/http"
	"time"
)

type VoucherCodeRequest struct {
	PhoneNumber string `json:"phoneNumber"`
	TransID     string `json:"transId"`
}

type VoucherCodeResponse struct {
	VoucherCodeRequest
	VoucherCode string `json:"voucherCode"`
}

var letters = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

func randSeq(n int) string {
	b := make([]rune, n)
	for i := range b {
		b[i] = letters[rand.Intn(len(letters))]
	}
	return string(b)
}

func voucherCode(w http.ResponseWriter, r *http.Request) {

	switch r.Method {
	case "POST":
		// Decode the JSON in the body and overwrite 'tom' with it
		d := json.NewDecoder(r.Body)
		p := &VoucherCodeRequest{}
		err := d.Decode(p)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
		time.Sleep(time.Duration(rand.Intn(1000)) * time.Millisecond)

		j, _ := json.Marshal(&VoucherCodeResponse{
			VoucherCodeRequest: *p,
			VoucherCode:        randSeq(10),
		})
		w.Header().Add("Content-Type", "application/json")
		w.Write(j)
	default:
		w.WriteHeader(http.StatusMethodNotAllowed)
		fmt.Fprintf(w, "I can't do that.")
	}
}

func main() {
	http.HandleFunc("/fake-api", voucherCode)

	log.Println("Go!")
	http.ListenAndServe(":8083", nil)
}
