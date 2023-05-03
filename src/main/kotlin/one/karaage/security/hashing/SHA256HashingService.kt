package one.karaage.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class SHA256HashingService: HashingService {
    /**
     * Adding a seed makes hashing more resistant to rainbow table attacks
     * generate seed -> toString() -> compute salt and value hash
     */
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val saltRaw = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val salt = Hex.encodeHexString(saltRaw)
        val hash = DigestUtils.sha256Hex("$salt$value")
        //println("salt + password: $salt$value = $hash")

        return SaltedHash(
            hash = hash,
            salt = salt
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        val hash = DigestUtils.sha256Hex("${saltedHash.salt}${value}")
        //println("salt + password: ${saltedHash.salt}${value} = $hash")
        return hash == saltedHash.hash
    }
}