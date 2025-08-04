resource "aws_s3_bucket" "bck_viajava" {
  bucket = var.bucket_name

  force_destroy = true
}


resource "aws_s3_bucket_public_access_block" "block_public_access" {
  bucket = aws_s3_bucket.bck_viajava.id

  block_public_acls = true
  block_public_policy = true
  ignore_public_acls = true
  restrict_public_buckets = true

}

resource "aws_s3_bucket_server_side_encryption_configuration" "bucket_encryption_configuration" {
  bucket = aws_s3_bucket.bck_viajava.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}