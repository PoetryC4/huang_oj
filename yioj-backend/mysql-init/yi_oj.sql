-- MariaDB dump 10.19  Distrib 10.4.25-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: yi_oj
-- ------------------------------------------------------
-- Server version	10.4.25-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api_keys`
--

DROP TABLE IF EXISTS `api_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accessKey` varchar(255) NOT NULL,
  `secretKey` varchar(255) NOT NULL,
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_accessKey` (`accessKey`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='签证';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_keys`
--

LOCK TABLES `api_keys` WRITE;
/*!40000 ALTER TABLE `api_keys` DISABLE KEYS */;
INSERT INTO `api_keys` VALUES (1,'KS8d794nxMf4o53gDSepIlxhJYwa3hpx','LOzTZrd4KjfvnSw26c8HLBqlTSkTHW9A5rmOYzEfvX2EU73ykb5rxQFR06oPnnrt','2023-10-25 16:09:57','2023-10-27 15:59:09',0);
/*!40000 ALTER TABLE `api_keys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `problemId` bigint(20) NOT NULL COMMENT '题目id',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `content` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `thumbNum` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1716356240482869250 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1716332244412448770,1,1709051634459385857,'test',1,'2023-10-18 13:54:29','2023-11-03 22:24:42',0),(1716356240482869249,1,1709051634459385857,'雪子',0,'2023-10-23 15:29:50','2023-10-26 21:10:16',0);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_thumb`
--

DROP TABLE IF EXISTS `comment_thumb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_thumb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `commentId` bigint(20) NOT NULL COMMENT '评论 id',
  `userId` bigint(20) NOT NULL COMMENT '用户 id',
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_commentId` (`commentId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_thumb`
--

LOCK TABLES `comment_thumb` WRITE;
/*!40000 ALTER TABLE `comment_thumb` DISABLE KEYS */;
INSERT INTO `comment_thumb` VALUES (4,1716332244412448770,1709051634459385857,'2023-10-31 23:12:08','2023-10-31 23:12:08');
/*!40000 ALTER TABLE `comment_thumb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem`
--

DROP TABLE IF EXISTS `problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '题目标题',
  `solution` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '官解',
  `content` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `difficulty` int(11) NOT NULL DEFAULT 0 COMMENT '难度',
  `tags` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '题目标签列表' CHECK (json_valid(`tags`)),
  `judgeConfig` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '判题标签列表' CHECK (json_valid(`judgeConfig`)),
  `judgeCases` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '检测样例' CHECK (json_valid(`judgeCases`)),
  `thumbNum` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `disLikeNum` int(11) NOT NULL DEFAULT 0 COMMENT '点踩数',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `functionConfig` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '函数配置' CHECK (json_valid(`functionConfig`)),
  `isVip` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'vip尊享?',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem`
--

LOCK TABLES `problem` WRITE;
/*!40000 ALTER TABLE `problem` DISABLE KEYS */;
INSERT INTO `problem` VALUES (1,'Two Sum','### Approach 3: One-pass Hash Table\n\n#### Algorithm\n\nIt turns out we can do it in one-pass. While we are iterating and inserting elements into the hash table, we also look back to check if current element\'s complement already exists in the hash table. If it exists, we have found a solution and return the indices immediately.','Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nYou can return the answer in any order.\n\n#### Example 1\n**Input:** nums = [2,7,11,15], target = 9\n\n**Output:** [0,1]\n\n**Explanation:** Because nums[0] + nums[1] == 9, we return [0, 1].\n\n#### Example 2\n**Input:** nums = [3,2,4], target = 6\n\n**Output:** [1,2]\n#### Example 3\n**Input:** nums = [3,3], target = 6\n\n**Output:** [0,1]\n\n### Constraints\n\n- 2 <= nums.length <= 10<sup>2</sup>\n- -10<sup>9</sup> <= nums[i] <= 10<sup>9</sup>\n- -10<sup>9</sup> <= target <= 10<sup>9</sup>\n- **Only one valid answer exists.**\n\n#### Follow-up\n\n Can you come up with an algorithm that is less than O(n<sup>2</sup>) time complexity?',0,'[\"hashmap\"]','{\"hint\":\"A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it\'s best to try out brute force solutions for just for completeness. It is from these brute force solutions that you can come up with optimizations.\",\"memoryLimit\":128,\"stackLimit\":128,\"testCaseProvided\":2,\"timeLimit\":1000}','{\"expected\":\"[0, 1]\\n[1, 2]\",\"input\":\"[2,7,11,15]\\n9\\n[3,2,4]\\n6\"}',1,0,1709051634459385857,'2023-10-09 23:28:37','2023-11-04 13:03:59','{\"correctCode\":{\"python\":\"\",\"java\":\"public class Solution {\\r\\n    public int[] twoSum(int[] nums, int target) {\\r\\n        Map<Integer,Integer> hm=new HashMap<>(nums.length);\\r\\n        for(int i=0;i<nums.length;i++){\\r\\n            if(hm.containsKey(target-nums[i])){\\r\\n                return new int[]{i,hm.get(target-nums[i])};\\r\\n            }\\r\\n            else hm.put(nums[i],i);\\r\\n        }\\r\\n        return new int[]{1,0};\\r\\n    }\\r\\n}\",\"javascript\":\"/**\\r\\n * @param {number[]} nums\\r\\n * @param {number} target\\r\\n * @return {number[]}\\r\\n */\\r\\nvar twoSum = function(nums, target) {\\r\\n    const numToIndex = new Map();\\r\\n    for (let i = 0; i < nums.length; i++) {\\r\\n        if (numToIndex.has(target - nums[i])) {\\r\\n            return [numToIndex.get(target - nums[i]), i];\\r\\n        }\\r\\n        numToIndex.set(nums[i], i);\\r\\n    }\\r\\n    return null;\\r\\n};\"},\"defaultCode\":{\"java\":\"class Solution {\\r\\n    public int[] twoSum(int[] nums, int target) {\\r\\n        \\r\\n    }\\r\\n}\",\"javascript\":\"/**\\r\\n * @param {number[]} nums\\r\\n * @param {number} target\\r\\n * @return {number[]}\\r\\n */\\r\\nvar twoSum = function(nums, target) {\\r\\n    \\r\\n};\"},\"initCode\":{\"java\":\"import com.alibaba.fastjson2.JSON;\\r\\n\\r\\nimport java.util.*;\\r\\n\\r\\npublic class Main {\\r\\n    private static Solution solution;\\r\\n\\r\\n    public static void main(String[] args) {\\r\\n        String numsStr = args[0];\\r\\n        String targetStr = args[1];\\r\\n\\r\\n        solution = new Solution();\\r\\n        List<Integer> numsList = JSON.parseArray(numsStr, Integer.class);\\r\\n        int[] nums = new int[numsList.size()];\\r\\n        for (int i = 0; i < numsList.size(); i++) {\\r\\n            nums[i] = numsList.get(i);\\r\\n        }\\r\\n        int target = Integer.parseInt(targetStr);\\r\\n        int[] output = solution.twoSum(nums, target);\\r\\n        System.out.println(\\\"@StdOutEnd;\\\");//stdOut结束符\\r\\n\\r\\n        if (output == null) {\\r\\n            System.out.println(output);\\r\\n        } else {\\r\\n            Arrays.sort(output);\\r\\n            System.out.println(Arrays.toString(output));\\r\\n        }\\r\\n        return;\\r\\n    }\\r\\n}\",\"javascript\":\"var main = function(...args) {\\r\\n    let nums = getJSON(args[0])\\r\\n    let target = parseInt(args[1])\\r\\n    let output = twoSum(nums, target)\\r\\n    console.log(\\\"@StdOutEnd;\\\")\\r\\n    if (output === null) {\\r\\n        console.log(output);\\r\\n    } else {\\r\\n        output.sort();\\r\\n        console.log(output);\\r\\n    }\\r\\n    return;\\r\\n}\\r\\n\\r\\nfunction getJSON(str) {\\r\\n    try {\\r\\n      let res = JSON.parse(str);\\r\\n      return res;\\r\\n    } catch (e) {\\r\\n      return false;\\r\\n    }\\r\\n}\\r\\n\\r\\nmain(...process.argv.slice(2));\"},\"validCode\":{\"java\":\"import com.alibaba.fastjson2.JSON;\\r\\n\\r\\nimport java.util.HashMap;\\r\\nimport java.util.List;\\r\\nimport java.util.Map;\\r\\n\\r\\npublic class Valid {\\r\\n    public static void main(String[] args) {\\r\\n        String numsStr = args[0];\\r\\n        String targetStr = args[1];\\r\\n\\r\\n        List<Integer> nums = JSON.parseArray(numsStr, Integer.class);\\r\\n        int target = Integer.parseInt(targetStr);\\r\\n\\r\\n        if (nums.size() < 2 || nums.size() > 100) {\\r\\n            System.out.println(\\\"nums长度过短或过长\\\");\\r\\n            return;\\r\\n        }\\r\\n        for (Integer num : nums) {\\r\\n            if (num < -1000000000 || num > 1000000000) {\\r\\n                System.out.println(\\\"nums元素大小超出范围\\\");\\r\\n                return;\\r\\n            }\\r\\n        }\\r\\n        if (target < -1000000000 || target > 1000000000) {\\r\\n            System.out.println(\\\"target大小超出范围\\\");\\r\\n            return;\\r\\n        }\\r\\n        int validCount = 0; // 检测条件:Only one valid answer exists\\r\\n        Map<Integer, Integer> hm = new HashMap<>(nums.size());\\r\\n        for (int j = 0; j < nums.size(); j++) {\\r\\n            if (hm.containsKey(target - nums.get(j))) {\\r\\n                validCount++;\\r\\n            } else hm.put(nums.get(j), j);\\r\\n        }\\r\\n        hm.clear();\\r\\n        if (validCount != 1) {\\r\\n            System.out.println(\\\"输入数据得到的答案不存在或有一个以上\\\");\\r\\n            return;\\r\\n        }\\r\\n        System.out.println(\\\"@ValidOK;\\\");\\r\\n        return;\\r\\n    }\\r\\n}\\r\\n\",\"javascript\":\"var valid = (...args) => {\\r\\n    let nums = getJSON(args[0])\\r\\n    if(nums == null) {\\r\\n        console.log(\\\"nums输入需要为有效数组\\\");\\r\\n        return;\\r\\n    }\\r\\n    if(nums.length < 2 || nums.length>100) {\\r\\n        console.log(\\\"nums长度过短或过长\\\");\\r\\n        return;\\r\\n    }\\r\\n    for(let num of nums) {\\r\\n        if(num < -1000000000 || num > 1000000000) {\\r\\n            console.log(\\\"nums元素大小超出范围\\\");\\r\\n            return;\\r\\n        }\\r\\n    }\\r\\n    let target = parseInt(args[1]);\\r\\n    if(target == null || isNaN(target)) {\\r\\n        console.log(\\\"target输入需要为有效整型\\\");\\r\\n        return;\\r\\n    }\\r\\n    if (target < -1000000000 || target > 1000000000) {\\r\\n        console.log(\\\"target大小超出范围\\\");\\r\\n        return;\\r\\n    }\\r\\n    let validCount = 0\\r\\n\\r\\n    const numToIndex = new Map();\\r\\n    for (let i = 0; i < nums.length; i++) {\\r\\n        if (numToIndex.has(target - nums[i])) {\\r\\n            validCount++\\r\\n        }\\r\\n        numToIndex.set(nums[i], i);\\r\\n    }\\r\\n    if (validCount !== 1) {\\r\\n        console.log(\\\"输入数据得到的答案不存在或有一个以上\\\");\\r\\n        return;\\r\\n    }\\r\\n    console.log(\\\"@ValidOK;\\\");\\r\\n    return;\\r\\n}\\r\\n\\r\\nfunction getJSON(str) {\\r\\n    try {\\r\\n      let res = JSON.parse(str);\\r\\n      return res;\\r\\n    } catch (e) {\\r\\n      return null;\\r\\n    }\\r\\n}\\r\\n\\r\\nvalid(...process.argv.slice(2));\"},\"varCount\":2,\"varNames\":[\"nums\",\"target\"]}',0,0);
/*!40000 ALTER TABLE `problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem_dislike`
--

DROP TABLE IF EXISTS `problem_dislike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_dislike` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `problemId` bigint(20) NOT NULL COMMENT '题目 id',
  `userId` bigint(20) NOT NULL COMMENT '用户 id',
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_problemId` (`problemId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='题目点踩';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem_dislike`
--

LOCK TABLES `problem_dislike` WRITE;
/*!40000 ALTER TABLE `problem_dislike` DISABLE KEYS */;
/*!40000 ALTER TABLE `problem_dislike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem_thumb`
--

DROP TABLE IF EXISTS `problem_thumb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_thumb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `problemId` bigint(20) NOT NULL COMMENT '题目 id',
  `userId` bigint(20) NOT NULL COMMENT '用户 id',
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_problemId` (`problemId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='题目点赞';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem_thumb`
--

LOCK TABLES `problem_thumb` WRITE;
/*!40000 ALTER TABLE `problem_thumb` DISABLE KEYS */;
INSERT INTO `problem_thumb` VALUES (13,1,1709051634459385857,'2023-10-22 22:17:06','2023-10-22 22:17:06');
/*!40000 ALTER TABLE `problem_thumb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `problemId` bigint(20) NOT NULL COMMENT '题目 id',
  `userId` bigint(20) NOT NULL COMMENT '提交用户 id',
  `language` varchar(128) NOT NULL COMMENT '语言',
  `code` text DEFAULT NULL COMMENT '提交的代码',
  `judgeStatus` tinyint(4) DEFAULT NULL COMMENT '当前状态 0-编译 1-运行 2-失败 3-成功',
  `judgeResult` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结果 ' CHECK (json_valid(`judgeResult`)),
  `submitTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '提交时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_postId` (`problemId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1720465996001107970 DEFAULT CHARSET=utf8mb4 COMMENT='提交';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission`
--

LOCK TABLES `submission` WRITE;
/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
INSERT INTO `submission` VALUES (1719728036490260482,1,1709051634459385857,'JavaScript','/**\r\n * @param {number[]} nums\r\n * @param {number} target\r\n * @return {number[]}\r\n */\r\nvar twoSum = function(nums, target) {\r\n    const numToIndex = new Map();\r\n    for (let i = 0; i < nums.length; i++) {\r\n        if (numToIndex.has(target - nums[i])) {\r\n            return [numToIndex.get(target - nums[i]), i];\r\n        }\r\n        numToIndex.set(nums[i], i);\r\n    }\r\n    return null;\r\n};',0,'{\"judgeInfo\":{\"memoryUsed\":0,\"resultStr\":\"Accepted\",\"timeUsed\":238},\"submissionId\":1719728036490260482}','2023-11-01 22:48:09',0),(1720465996001107969,1,1709051634459385857,'JavaScript','/**\r\n * @param {number[]} nums\r\n * @param {number} target\r\n * @return {number[]}\r\n */\r\nvar twoSum = function(nums, target) {\r\n    const numToIndex = new Map();\r\n    for (let i = 0; i < nums.length; i++) {\r\n        if (numToIndex.has(target - nums[i])) {\r\n            return [numToIndex.get(target - nums[i]), i];\r\n        }\r\n        numToIndex.set(nums[i], i);\r\n    }\r\n    return null;\r\n};',0,'{\"judgeInfo\":{\"memoryUsed\":0,\"resultStr\":\"Accepted\",\"timeUsed\":293},\"submissionId\":1720465996001107969}','2023-11-03 23:40:32',0);
/*!40000 ALTER TABLE `submission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userSalt` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '盐',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '用户' COMMENT '用户昵称',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userEmail` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `createTime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `isVip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否为VIP',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB AUTO_INCREMENT=1712398136665706498 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1709051634459385857,'hzy777','sqi72xj01bPN','665a74c1544864ebb7d3027e46d6aa67',NULL,NULL,'用户','1709051634459385857.jpg','drfvbdrb','1@qq.com','admin','2023-10-03 11:43:56','2023-10-22 20:43:53',0,0),(1712398136665706497,'hzy888','Ej4Wl8MrswCA','956e883a88d8c0e6739ad7c44cafd2ad',NULL,NULL,'用户',NULL,NULL,'473240934@qq.com','user','2023-10-12 17:21:45','2023-10-26 22:08:03',0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-05 13:25:10
