output "bucket_name" {
  description = "Name of the s3 bucket"
  value = aws_s3_bucket.bck_viajava.id
}

output "bucket_arn" {
  description = "Arn of s3 bucket"
  value = aws_s3_bucket.bck_viajava.arn
}

output "bucket_region" {
  description = "Region of s3 bucket"
  value = var.aws_region
}

output "bucket_domain_name" {
  description = "Endpoint to access bucket with HTTP/HTTPS"
  value = aws_s3_bucket.bck_viajava.bucket_domain_name
}

#
# output "bucket_website_endpoint" {
#   description = "Endpoint for web access"
#   value = aws_s3_bucket.bck_viajava.website_endpoint
# }