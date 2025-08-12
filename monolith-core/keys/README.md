Generate RSA keypair:

openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in private.pem -out public.pem

Encode public key to base64 (X.509 DER):
openssl rsa -pubin -in public.pem -pubout -outform DER | base64 > public.b64

Place private.pem and public.b64 here for local testing.
