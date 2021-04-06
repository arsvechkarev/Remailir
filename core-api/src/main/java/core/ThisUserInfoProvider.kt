package core

import core.model.ThisUserInfo

/**
 * Provides info about current user
 *
 * @see ThisUserInfoStorage
 */
interface ThisUserInfoProvider {
  
  fun getUserInfo(): ThisUserInfo
}