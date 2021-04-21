package ssh.internal

import com.hierynomus.sshj.key.KeyAlgorithms

val keyAlgorithms = List(
  //KeyAlgorithms.EdDSA25519(),
  KeyAlgorithms.ECDSASHANistp521(),
  KeyAlgorithms.ECDSASHANistp384(),
  KeyAlgorithms.ECDSASHANistp256(),
  KeyAlgorithms.RSASHA512(),
  KeyAlgorithms.RSASHA256(),
  KeyAlgorithms.SSHRSACertV01(),
  KeyAlgorithms.SSHDSSCertV01(),
  KeyAlgorithms.SSHRSA(),
  KeyAlgorithms.SSHDSA()
).map(_.nn)
