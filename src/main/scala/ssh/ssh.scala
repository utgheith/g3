package ssh

import geny.Generator
import geny.Generator.{Action, Continue}
import net.schmizz.sshj.{DefaultConfig, SSHClient}
//import ssh.Client.keyAlgorithms

import scala.jdk.CollectionConverters._

import ag.*
import ssh.internal.*

extension (it: SSHClient) {

  def bytes(cmd: String): Generator[Byte] = new Generator[Byte] {
    override def generate(h: Byte => Action): Action = {
      val s = it.startSession().nn
      try {
        val c = s.exec(cmd).nn
        try {
          val r = c.getInputStream.nn
          var arr = Array.ofDim[Byte](1024)
          var n = 1
          var a: Action = Continue
          while ((n > 0) && (a == Continue)) {
            n = r.read(arr)
            var i = 0
            while ((i < n) && (a == Continue)) {
              a = h(arr(i))
              i += 1
            }
          }
          a
        } finally {
          c.join()
          c.close()
          val rc = c.getExitStatus().nn
          if (rc.intValue != 0) {
            throw new Exception(s"remote command failed, rc=$rc")
          }
        }
      } finally {
        s.close()
      }

    }
  }

  def chars(cmd: String): Generator[Char] = {
    bytes(cmd).toChars
  }

  def lines(cmd: String): Generator[String] = {
    chars(cmd).toLines
  }
}

def ssh_client(user: String, host: String, loadKnownHosts: Boolean = true): SSHClient = {
  val config = new DefaultConfig()
  config.setKeyAlgorithms(keyAlgorithms.asJava)
  val it = SSHClient(config)
  if (loadKnownHosts) {
    it.loadKnownHosts()
  }
  it.connect(host)
  it.authPublickey(user)
  it
}




