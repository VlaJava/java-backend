terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = var.region

  default_tags {
    tags = {
      owner      = "emanuelernesto"
      managed-by = "terraform"
    }
  }
}

resource "random_id" "bucket_sufix" {
  byte_length = 8
}

module "s3_bucket" {
  source      = "./s3"
  bucket_name = "bck-viajava-assets-${random_id.bucket_sufix.hex}"
  aws_region  = var.region
}

