package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.DTO.VendorDTO;

@FeignClient(name = "invoice-service", url = "${invoice.service.url}")
public interface InvoiceUpdateFeignClient {

    @PutMapping("/manual-invoice/invoices/update-vendor")
    void updateInvoicesByVendor(@RequestBody VendorDTO vendorDTO);
}
