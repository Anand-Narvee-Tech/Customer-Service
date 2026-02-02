package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INVOICE-SERVICE", url = "${invoice.service.url}")
public interface InvoiceFeignClient {

	@GetMapping("/manual-invoice/invoices/count-by-vendor/{vendorId}")
	long countInvoicesByVendor(@PathVariable("vendorId") Long vendorId);

}
